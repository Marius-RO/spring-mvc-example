package com.company.controller;

import com.company.controller.util.AbstractController;
import com.mysql.cj.exceptions.CJCommunicationsException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Controller
@ControllerAdvice
@RequestMapping(path = ErrorController.PathHandler.BASE_URL)
public class ErrorController extends AbstractController {

    public interface ViewHandler {
        String ERROR_GENERAL = "error-general";
        String ERROR_MAX_UPLOAD = "error-max-upload";
        String ERROR_ACCESS_DENIED = "error-access-denied";
        String ERROR_DB_CONNECTION = "error-db-connection";
    }

    public interface PathHandler {
        String BASE_URL = "/error";

        String ERROR_GENERAL_URL = "/general";
        String FULL_ERROR_GENERAL_URL = BASE_URL + ERROR_GENERAL_URL;

        String ERROR_MAX_UPLOAD_URL = "/max-upload";
        String FULL_ERROR_MAX_UPLOAD_URL = BASE_URL + ERROR_MAX_UPLOAD_URL;

        String ERROR_ACCESS_DENIED_URL = "/access-denied";
        String FULL_ERROR_ACCESS_DENIED_URL = BASE_URL + ERROR_ACCESS_DENIED_URL;
    }

    public interface Security {
        String[] PERMIT_ALL = {
                PathHandler.FULL_ERROR_ACCESS_DENIED_URL,
                PathHandler.FULL_ERROR_GENERAL_URL
        };

        String[] PERMIT_ONLY_EMPLOYEE_ADMIN = {
                PathHandler.FULL_ERROR_MAX_UPLOAD_URL
        };
    }

    @Autowired
    public ErrorController(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    protected Class<?> getClassType() {
        return ErrorController.class;
    }

    @ExceptionHandler(value = Exception.class)
    public String handleAnyException(){
        return ViewHandler.ERROR_GENERAL;
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public String handleMaxUpload(){
        return ViewHandler.ERROR_MAX_UPLOAD;
    }

    @ExceptionHandler(value = {CJCommunicationsException.class, CommunicationsException.class, CannotGetJdbcConnectionException.class})
    public String handleJdbcConnection(){
        return ViewHandler.ERROR_DB_CONNECTION;
    }


    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    @RequestMapping(path = PathHandler.ERROR_ACCESS_DENIED_URL, method = RequestMethod.GET)
    public String getAccessDeniedPage(){
        return ViewHandler.ERROR_ACCESS_DENIED;
    }
}
