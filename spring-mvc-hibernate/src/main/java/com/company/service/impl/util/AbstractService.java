package com.company.service.impl.util;

import com.company.util.AbstractCommonControllerServiceRepository;
import org.springframework.web.context.WebApplicationContext;

public abstract class AbstractService extends AbstractCommonControllerServiceRepository {

    public AbstractService(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }
}
