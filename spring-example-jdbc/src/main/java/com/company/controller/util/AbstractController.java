package com.company.controller.util;

import com.company.util.AbstractCommonControllerServiceRepository;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractController extends AbstractCommonControllerServiceRepository {
    public AbstractController(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }
}
