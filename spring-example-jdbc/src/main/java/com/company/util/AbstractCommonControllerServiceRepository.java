package com.company.util;

import org.springframework.util.Assert;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractCommonControllerServiceRepository {

    private final Logger logger;

    public AbstractCommonControllerServiceRepository() {
        Class<?> classType = getClassType();
        Assert.notNull(classType, "classType can not be ull");
        this.logger = Logger.getLogger(classType.getName());
    }

    protected abstract Class<?> getClassType();

    protected void logInfo(String message){
        logger.log(Level.INFO, message);
    }

    protected void logError(String message){
        logger.log(Level.SEVERE, message);
    }

    protected void logDebug(String message){
        logger.log(Level.FINE, message);
    }

}
