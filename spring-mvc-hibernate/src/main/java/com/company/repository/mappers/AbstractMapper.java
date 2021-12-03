package com.company.repository.mappers;

import com.company.util.AbstractBaseClass;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractMapper extends AbstractBaseClass {

    public AbstractMapper(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }
}
