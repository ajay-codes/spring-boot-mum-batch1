package com.example.config;

import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor (BPP) - called for EVERY bean, before and after initialization.
 * Use case: custom logic around @PostConstruct, e.g. validation, wrapping with proxies.
 * Currently disabled (@Component commented out) to reduce log noise during demos.
 */
// @Component
public class BPP implements org.springframework.beans.factory.config.BeanPostProcessor {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger("shop-it-service");

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws org.springframework.beans.BeansException {
        logger.info("---------BPP--------");
        logger.info("Before Initialization: " + beanName);
        logger.info("---------BPP--------");
        return bean; // you can return any other object as well
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws org.springframework.beans.BeansException {
       logger.info("---------BPP--------");
        logger.info("After Initialization: " + beanName);
        logger.info("---------BPP--------");
        return bean; // you can return any other object as well
    }
    
}
