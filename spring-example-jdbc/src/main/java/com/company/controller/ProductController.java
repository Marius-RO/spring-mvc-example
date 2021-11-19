package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.dto.OrderDetailsDto;
import com.company.dto.ProductDto;
import com.company.model.Product;
import com.company.model.UserAccount;
import com.company.service.AccountService;
import com.company.service.ProductService;
import com.company.util.GeneralUtilities;
import com.company.util.SessionCart;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;

@Controller
@RequestMapping(path = ProductController.PathHandler.BASE_URL)
public class ProductController extends AbstractController {

    public interface ViewHandler {
        String PRODUCT_ADD = "product-add";
        String PRODUCT_DETAILS = "product-details";
        String PRODUCT_UPDATE = "product-update";
        String PRODUCT_DELETE_RESPONSE = "product-delete-response";
        String PRODUCT_CART = "product-cart";
    }

    public interface PathHandler {
        String BASE_URL = "/products";
        String PRODUCT_DETAILS_URL = "/details";
        String FULL_PRODUCT_DETAILS_URL = BASE_URL + PRODUCT_DETAILS_URL;

        String ADD_PRODUCT_URL = "/add";
        String FULL_ADD_PRODUCT_URL = BASE_URL + ADD_PRODUCT_URL;
        String PROCESS_ADD_PRODUCT_URL = "/process-add";
        String FULL_PROCESS_ADD_PRODUCT_URL = BASE_URL + PROCESS_ADD_PRODUCT_URL;

        String UPDATE_PRODUCT_URL = "/update";
        String FULL_UPDATE_PRODUCT_URL = BASE_URL + UPDATE_PRODUCT_URL;
        String PROCESS_UPDATE_PRODUCT_URL = "/process-update";
        String FULL_PROCESS_UPDATE_PRODUCT_URL = BASE_URL + PROCESS_UPDATE_PRODUCT_URL;

        String PROCESS_DELETE_PRODUCT_URL = "/process-delete";
        String FULL_PROCESS_DELETE_PRODUCT_URL = BASE_URL + PROCESS_DELETE_PRODUCT_URL;

        String PRODUCT_DELETE_RESPONSE_URL = "/delete-response";
        String FULL_PRODUCT_DELETE_RESPONSE_URL = BASE_URL + PRODUCT_DELETE_RESPONSE_URL;

        String PRODUCTS_CART_URL = "/cart";
        String FULL_PRODUCTS_CART_URL = BASE_URL + PRODUCTS_CART_URL;

        String PROCESS_ADD_TO_CART_URL = "/add-to-cart";
        String FULL_PROCESS_ADD_TO_CART_URL = BASE_URL + PROCESS_ADD_TO_CART_URL;

        String PROCESS_UPDATE_PRODUCT_QUANTITY_FROM_CART_URL = "/update-cart-quantity";
        String FULL_PROCESS_UPDATE_PRODUCT_QUANTITY_FROM_CART_URL = BASE_URL + PROCESS_UPDATE_PRODUCT_QUANTITY_FROM_CART_URL;

        String PROCESS_DELETE_FROM_CART_URL = "/delete-from-cart";
        String FULL_PROCESS_DELETE_FROM_CART_URL = BASE_URL + PROCESS_DELETE_FROM_CART_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {
                PathHandler.BASE_URL,
                PathHandler.FULL_PRODUCT_DETAILS_URL + "/*"
        };

        String[] PERMIT_ONLY_EMPLOYEE_ADMIN = {
                PathHandler.FULL_ADD_PRODUCT_URL,
                PathHandler.FULL_PROCESS_ADD_PRODUCT_URL,
                PathHandler.FULL_UPDATE_PRODUCT_URL + "/*",
                PathHandler.FULL_PROCESS_UPDATE_PRODUCT_URL + "/*",
                PathHandler.FULL_PROCESS_DELETE_PRODUCT_URL + "/*",
                PathHandler.FULL_PRODUCT_DELETE_RESPONSE_URL
        };


        String[] PERMIT_ONLY_VISITOR_CUSTOMER = {
                PathHandler.FULL_PRODUCTS_CART_URL,
                PathHandler.FULL_PROCESS_ADD_TO_CART_URL + "/*",
                PathHandler.FULL_PROCESS_UPDATE_PRODUCT_QUANTITY_FROM_CART_URL + "/**",
                PathHandler.FULL_PROCESS_DELETE_FROM_CART_URL + "/*"
        };
    }

    public final static String SESSION_CART_ATTRIBUTE_KEY = "sessionCart";

    private final ProductService productService;
    private final AccountService accountService;

    @Autowired
    public ProductController(WebApplicationContext webApplicationContext, ProductService productService, AccountService accountService) {
        super(webApplicationContext);
        this.productService = productService;
        this.accountService = accountService;
    }

    @Override
    protected Class<?> getClassType() {
        return ProductController.class;
    }

    @RequestMapping(path = PathHandler.ADD_PRODUCT_URL, method = RequestMethod.GET)
    public String getAddProductPage(@ModelAttribute("productDto") ProductDto productDto){
        return ViewHandler.PRODUCT_ADD;
    }

    @RequestMapping(path = PathHandler.PRODUCTS_CART_URL, method = RequestMethod.GET)
    public String getCartPage(Principal principal, Model model, HttpSession httpSession){
        if(httpSession == null){
            return RootController.ViewHandler.HOME;
        }

        Object object = httpSession.getAttribute(SESSION_CART_ATTRIBUTE_KEY);
        if(!(object instanceof SessionCart)){
            httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, webApplicationContext.getBean(SessionCart.class));
            return ViewHandler.PRODUCT_CART;
        }

        SessionCart sessionCart = (SessionCart) object;
        sessionCart.refreshCart();
        httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, sessionCart);

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        if(principal != null){
            UserAccount userAccount = accountService.getUser(principal.getName());
            if (userAccount != null){
                orderDetailsDto.setFullName(userAccount.getFirstName() + " " + userAccount.getLastName());
                orderDetailsDto.setFullAddress(userAccount.getAddress().getFullAddress() + " ["+ userAccount.getAddress().getPhone() + "]");
            }
        }

        model.addAttribute("orderDetailsDto", orderDetailsDto);
        return ViewHandler.PRODUCT_CART;
    }

    @RequestMapping(path = PathHandler.UPDATE_PRODUCT_URL + "/{productId}", method = RequestMethod.GET)
    public String getProductUpdatePage(@PathVariable("productId") int productId, Model model){
        model.addAttribute("productDto", getConversion(productService.getProductById(productId), productService.getProductCategoriesByProductId(productId)));
        return ViewHandler.PRODUCT_UPDATE;
    }

    @RequestMapping(path = PathHandler.PRODUCT_DELETE_RESPONSE_URL, method = RequestMethod.GET)
    public String getProductDeleteResponsePage(){
        return ViewHandler.PRODUCT_DELETE_RESPONSE;
    }

    @RequestMapping(path = PathHandler.PRODUCT_DETAILS_URL + "/{productId}", method = RequestMethod.GET)
    public String getProductDetailsPage(@PathVariable("productId") int productId, Model model, HttpSession httpSession){
        Product product = productService.getProductById(productId);

        if(httpSession != null){
            Object object = httpSession.getAttribute(ProductController.SESSION_CART_ATTRIBUTE_KEY);
            if(object instanceof SessionCart){
                SessionCart sessionCart = (SessionCart) object;
                product.setAddedToCart(sessionCart.checkIfProductIsAdded(productId));
            }
        }

        model.addAttribute("productDto", getConversion(product, productService.getProductCategoriesByProductId(productId)));
        return ViewHandler.PRODUCT_DETAILS;
    }

    @RequestMapping(path = PathHandler.PROCESS_ADD_PRODUCT_URL, method = RequestMethod.POST)
    public String processAddProduct(@Valid @ModelAttribute("productDto") ProductDto productDto,
                                    BindingResult bindingResult,
                                    Model model,
                                    Principal principal){
        if(bindingResult.hasErrors()){
            return ViewHandler.PRODUCT_ADD;
        }

        if(principal == null){
            model.addAttribute("attributeErrorAddProduct", Boolean.TRUE);
            return ViewHandler.PRODUCT_ADD;
        }

        if(!productService.isAddedImageFileValid(productDto.getImageMultipartFile())){
            model.addAttribute("attributeErrorImage", Boolean.TRUE);
            return ViewHandler.PRODUCT_ADD;
        }

        int productId = productService.addProductAndGetId(productDto, principal.getName());
        model.addAttribute("attributeSuccessAddProduct", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_PRODUCT_DETAILS_URL + "/" + productId;
    }

    @RequestMapping(path = PathHandler.PROCESS_UPDATE_PRODUCT_URL + "/{productId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public String processUpdateProduct(@PathVariable("productId") int productId,
                                        @Valid @ModelAttribute("productDto") ProductDto productDto,
                                        BindingResult bindingResult,
                                        Model model,
                                        Principal principal){
        if(bindingResult.hasErrors()){
            return ViewHandler.PRODUCT_UPDATE;
        }

        if(principal == null){
            model.addAttribute("attributeErrorUpdateProduct", Boolean.TRUE);
            return ViewHandler.PRODUCT_UPDATE;
        }

        productService.updateProductById(productDto, productId, principal.getName());
        model.addAttribute("attributeSuccessUpdateProduct", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_PRODUCT_DETAILS_URL + "/" + productId;
    }

    @RequestMapping(path = PathHandler.PROCESS_DELETE_PRODUCT_URL + "/{productId}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public String processDeleteProduct(@PathVariable("productId") int productId,Principal principal, Model model){
        if(principal == null){
            model.addAttribute("attributeErrorDeleteProduct", Boolean.TRUE);
            return GeneralUtilities.REDIRECT + PathHandler.FULL_PRODUCT_DELETE_RESPONSE_URL;
        }
        productService.deleteProductById(productId, principal.getName());
        model.addAttribute("attributeSuccessDeleteProduct", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_PRODUCT_DELETE_RESPONSE_URL;
    }

    private ProductDto getConversion(Product product, int[] categoriesIds){
        ProductDto productDto = new ProductDto();
        productDto.setPrice(new BigDecimal(String.valueOf(product.getPrice())).setScale(2, RoundingMode.HALF_EVEN));
        productDto.setStock(product.getStock());
        productDto.setDescription(product.getDescription());
        productDto.setName(product.getName());
        productDto.setCategoriesIds(categoriesIds);
        productDto.setImageMultipartFile(null);
        productDto.setImageBase64(product.getImageBase64());
        productDto.setAddedToCart(product.isAddedToCart());

        return productDto;
    }

    @RequestMapping(path = PathHandler.PROCESS_ADD_TO_CART_URL + "/{productId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Integer> processAddToCart(@PathVariable("productId") int productId, HttpSession httpSession){
        if(httpSession == null){
            return new ResponseEntity<>(0, HttpStatus.OK);
        }

        SessionCart sessionCart;
        Object object = httpSession.getAttribute(SESSION_CART_ATTRIBUTE_KEY);
        if(!(object instanceof SessionCart)){
            sessionCart = webApplicationContext.getBean(SessionCart.class);
        }
        else {
            sessionCart = (SessionCart) object;
        }

        sessionCart.addProduct(productId);
        httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, sessionCart);
        return new ResponseEntity<>(sessionCart.cartSize(), HttpStatus.OK);
    }

    @RequestMapping(path = PathHandler.PROCESS_UPDATE_PRODUCT_QUANTITY_FROM_CART_URL + "/{productId}/{newQuantity}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<String> processUpdateQuantityOfCartProduct(@PathVariable("productId") int productId,
                                                                   @PathVariable("newQuantity") int newQuantity,
                                                                   HttpSession httpSession){
        CartResponse cartResponse = new CartResponse(0, 0.0);
        if(httpSession == null){
            return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
        }

        Object object = httpSession.getAttribute(SESSION_CART_ATTRIBUTE_KEY);
        if(!(object instanceof SessionCart)){
            return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
        }

        SessionCart sessionCart = (SessionCart) object;
        sessionCart.updateProductQuantity(productId, newQuantity);
        httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, sessionCart);

        cartResponse.setCartItems(sessionCart.cartSize());
        cartResponse.setTotalAmount(sessionCart.getTotalAmount());
        return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
    }

    @RequestMapping(path = PathHandler.PROCESS_DELETE_FROM_CART_URL + "/{productId}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<String> processDeleteFromCart(@PathVariable("productId") int productId, HttpSession httpSession){
        CartResponse cartResponse = new CartResponse(0, 0.0);
        if(httpSession == null){
            return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
        }

        Object object = httpSession.getAttribute(SESSION_CART_ATTRIBUTE_KEY);
        if(!(object instanceof SessionCart)){
            httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, webApplicationContext.getBean(SessionCart.class));
            return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
        }

        SessionCart sessionCart = (SessionCart) object;
        sessionCart.removeProduct(productId);
        httpSession.setAttribute(SESSION_CART_ATTRIBUTE_KEY, sessionCart);

        cartResponse.setCartItems(sessionCart.cartSize());
        cartResponse.setTotalAmount(sessionCart.getTotalAmount());
        return new ResponseEntity<>(new Gson().toJson(cartResponse, CartResponse.class), HttpStatus.OK);
    }

    @Getter
    @Setter
    private static class CartResponse {
        private int cartItems;
        private double totalAmount;

        public CartResponse(int cartItems, double totalAmount) {
            this.cartItems = cartItems;
            this.totalAmount = totalAmount;
        }
    }

}
