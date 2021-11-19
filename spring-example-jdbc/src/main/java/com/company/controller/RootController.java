package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.model.Product;
import com.company.service.ProductService;
import com.company.util.SessionCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = RootController.PathHandler.BASE_URL)
public class RootController extends AbstractController {

    public interface ViewHandler {
        String HOME = "index";
        String ACCESS_DENIED = "error-access-denied";
    }

    public interface PathHandler {
        String BASE_URL = "/";
        String HOME_URL = "index";
        String FULL_HOME_URL = BASE_URL + HOME_URL;
        String ACCESS_DENIED_URL = "access-denied";
        String FULL_ACCESS_DENIED_URL = BASE_URL + ACCESS_DENIED_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {
                PathHandler.FULL_HOME_URL,
                PathHandler.FULL_ACCESS_DENIED_URL
        };
    }

    private final ProductService productService;

    @Autowired
    public RootController(WebApplicationContext webApplicationContext, ProductService productService) {
        super(webApplicationContext);
        this.productService = productService;
    }

    @Override
    protected Class<?> getClassType() {
        return RootController.class;
    }

    @RequestMapping(path = PathHandler.HOME_URL, method = RequestMethod.GET)
    public String getHomePage(Model model, HttpSession httpSession){
        final int limit = 8;
        final int halfLimit = 4;
        List<Product> productList = productService.getLastAddedProducts(limit);

        if(productList.isEmpty()){
            model.addAttribute("firstSlideProductList", new ArrayList<>());
            model.addAttribute("secondSlideProductList", new ArrayList<>());
            return ViewHandler.HOME;
        }

        if(httpSession != null){
            Object object = httpSession.getAttribute(ProductController.SESSION_CART_ATTRIBUTE_KEY);
            if(object instanceof SessionCart){
                SessionCart sessionCart = (SessionCart) object;
                productList.forEach(pr -> pr.setAddedToCart(sessionCart.checkIfProductIsAdded(pr.getId())));
            }
        }

        if(productList.size() <= halfLimit){
            model.addAttribute("firstSlideProductList", productList);
            model.addAttribute("secondSlideProductList", new ArrayList<>());
            return ViewHandler.HOME;
        }

        model.addAttribute("firstSlideProductList", productList.subList(0, halfLimit));
        model.addAttribute("secondSlideProductList", productList.subList(halfLimit, productList.size()));
        return ViewHandler.HOME;
    }

    @RequestMapping(path = PathHandler.ACCESS_DENIED_URL, method = RequestMethod.GET)
    public String getAccessDeniedPage(){
        return ViewHandler.ACCESS_DENIED;
    }

}
