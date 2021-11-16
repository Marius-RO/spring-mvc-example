package com.company.controller;

import com.company.controller.util.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.WebApplicationContext;

@ControllerAdvice
public class ErrorController extends AbstractController {

    @Autowired
    public ErrorController(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    protected Class<?> getClassType() {
        return ErrorController.class;
    }

    /*
    @ExceptionHandler(value = Exception.class)
    public String handleAnyException(){
        return "general-error";
    }
     */

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }
}
