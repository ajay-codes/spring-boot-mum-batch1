package com.example.config;

import org.springframework.stereotype.Component;

/**
 * BeanPostProcessor (BPP) - intercepts every bean AFTER instantiation,
 * before and after the init-method (@PostConstruct) callback.
 * Disabled (@Component commented out) to reduce log noise; uncomment to observe the lifecycle.
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
