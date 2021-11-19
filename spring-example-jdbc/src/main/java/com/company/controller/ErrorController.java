package com.company.controller;

import com.company.controller.util.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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


//    @ExceptionHandler(value = Exception.class)
//    public String handleAnyException(){
//        return "error-general";
//    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public String handleMaxUpload(){
        return "error-max-upload";
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }
}
