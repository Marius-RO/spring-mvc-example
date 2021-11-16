package com.company.controller;

import com.company.controller.util.AbstractController;
import com.company.dto.RegisterDto;
import com.company.service.AccountService;
import com.company.util.GeneralUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;

@Controller
@RequestMapping(path = AccountController.PathHandler.BASE_URL)
public class AccountController extends AbstractController {

    public interface ViewHandler {
        String LOGIN = "login";
        String REGISTER = "register";
    }

    public interface PathHandler {
        String BASE_URL = "/account";
        String LOGIN_URL = "/login";
        String FULL_LOGIN_URL = BASE_URL + LOGIN_URL;
        String REGISTER_URL = "/register";
        String FULL_REGISTER_URL = BASE_URL + REGISTER_URL;
        String PROCESS_REGISTER_URL = "/process-register";
        String FULL_PROCESS_REGISTER_URL = BASE_URL + PROCESS_REGISTER_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {
                PathHandler.FULL_LOGIN_URL,
                PathHandler.FULL_REGISTER_URL,
                PathHandler.FULL_PROCESS_REGISTER_URL
        };
    }

    private final AccountService accountService;

    @Autowired
    public AccountController(WebApplicationContext webApplicationContext, AccountService accountService) {
        super(webApplicationContext);
        this.accountService = accountService;
    }

    @Override
    protected Class<?> getClassType() {
        return AccountController.class;
    }

    @RequestMapping(path = PathHandler.LOGIN_URL, method = RequestMethod.GET)
    public String getLoginPage(){
        return ViewHandler.LOGIN;
    }

    @RequestMapping(path = PathHandler.REGISTER_URL, method = RequestMethod.GET)
    public String getRegisterPage(@ModelAttribute("registerDto") RegisterDto registerDto){
        return ViewHandler.REGISTER;
    }

    @RequestMapping(path = PathHandler.PROCESS_REGISTER_URL, method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("registerDto") RegisterDto registerDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ViewHandler.REGISTER;
        }
        accountService.registerUser(registerDto);
        return GeneralUtilities.REDIRECT + ViewHandler.LOGIN + "?successRegister";
    }

}
