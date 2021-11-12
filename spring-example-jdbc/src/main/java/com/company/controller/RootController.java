package com.company.controller;

import com.company.controller.util.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/")
public class RootController extends AbstractController {

    public interface ViewHandler {
        String HOME = "index";
        String ACCESS_DENIED = "access-denied";
    }

    public interface PathHandler {
        String BASE_URL = "/";
        String HOME_URL = "/index";
        String FULL_HOME_URL = BASE_URL + HOME_URL;
        String ACCESS_DENIED_URL = "/access-denied";
        String FULL_ACCESS_DENIED_URL = BASE_URL + ACCESS_DENIED_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {PathHandler.FULL_HOME_URL, PathHandler.FULL_ACCESS_DENIED_URL};
    }

    @Override
    protected Class<?> getClassType() {
        return RootController.class;
    }

    @RequestMapping(path = PathHandler.HOME_URL, method = RequestMethod.GET)
    public String getHomePage(){
        return ViewHandler.HOME;
    }

    @RequestMapping(path = PathHandler.ACCESS_DENIED_URL, method = RequestMethod.GET)
    public String getAccessDeniedPage(){
        return ViewHandler.ACCESS_DENIED;
    }

}
