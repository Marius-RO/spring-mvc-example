package com.company.controller;

import com.company.config.WebAppSecurityConfig;
import com.company.controller.util.AbstractController;
import com.company.dto.OrderDetailsDto;
import com.company.model.Order;
import com.company.service.OrderService;
import com.company.util.GeneralUtilities;
import com.company.util.SessionCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = OrderController.PathHandler.BASE_URL)
public class OrderController extends AbstractController {

    public interface ViewHandler {
        String ORDERS = "orders";
    }

    public interface PathHandler {
        String BASE_URL = "/orders";

        String ORDERS_URL = "/all";
        String FULL_ORDERS_URL = BASE_URL + ORDERS_URL;

        String PROCESS_SEND_ORDER_URL = "/process-send";
    }

    public interface Security {
        String[] PERMIT_ONLY_CUSTOMER_EMPLOYEE_ADMIN = {
                PathHandler.BASE_URL + "/*"
        };
    }

    private final OrderService orderService;

    @Override
    protected Class<?> getClassType() {
        return OrderController.class;
    }

    @Autowired
    public OrderController(WebApplicationContext webApplicationContext, OrderService orderService) {
        super(webApplicationContext);
        this.orderService = orderService;
    }

    @RequestMapping(path = PathHandler.ORDERS_URL, method = RequestMethod.GET)
    public String getOrdersPage(Authentication authentication, Model model){
        if(authentication == null || !authentication.isAuthenticated()){
            return ErrorController.ViewHandler.ERROR_ACCESS_DENIED;
        }

        boolean isCustomer = false;
        for(GrantedAuthority authority : authentication.getAuthorities()){
            if(WebAppSecurityConfig.Roles.ROLE_CUSTOMER.equals(authority.getAuthority())){
                isCustomer = true;
                break;
            }
        }

        List<Order> orderList;
        if(isCustomer){
            orderList = orderService.getOrdersByUserEmail(authentication.getName());
        }
        else {
            orderList = orderService.getAllOrders();
        }

        model.addAttribute("orderList", orderList);
        return ViewHandler.ORDERS;
    }

    @RequestMapping(path = PathHandler.PROCESS_SEND_ORDER_URL, method = RequestMethod.POST)
    public String processSendOrder(@Valid @ModelAttribute("orderDetailsDto") OrderDetailsDto orderDetailsDto,
                                    BindingResult bindingResult,
                                    Model model,
                                    Principal principal,
                                    HttpSession httpSession){
        if(bindingResult.hasErrors()){
            model.addAttribute("attributeErrorAddress", Boolean.TRUE);
            return ProductController.ViewHandler.PRODUCT_CART;
        }

        if(principal == null || httpSession == null){
            model.addAttribute("attributeErrorSendOrder", Boolean.TRUE);
            return ProductController.ViewHandler.PRODUCT_CART;
        }

        Object object = httpSession.getAttribute(ProductController.SESSION_CART_ATTRIBUTE_KEY);
        if(!(object instanceof SessionCart)){
            model.addAttribute("attributeErrorSendOrder", Boolean.TRUE);
            return ProductController.ViewHandler.PRODUCT_CART;
        }

        SessionCart sessionCart = (SessionCart) object;
        if(!sessionCart.isCartValid()){
            model.addAttribute("attributeErrorSendOrder", Boolean.TRUE);
            return ProductController.ViewHandler.PRODUCT_CART;
        }

        orderService.insertOrder(orderDetailsDto, sessionCart, principal.getName());
        sessionCart.clearCart();
        httpSession.setAttribute(ProductController.SESSION_CART_ATTRIBUTE_KEY, sessionCart);

        model.addAttribute("attributeSuccessSendOrder", Boolean.TRUE);
        return GeneralUtilities.REDIRECT + PathHandler.FULL_ORDERS_URL;
    }

}
