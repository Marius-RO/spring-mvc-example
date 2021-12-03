package com.company.util;

import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractBaseClass {

    protected final WebApplicationContext webApplicationContext;

    public AbstractBaseClass(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }
}
