package com.cisdi.enfi.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringFactory implements ApplicationContextAware {
    private static ApplicationContext context;

    public SpringFactory() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static Object getObject(String id) {
        Object object = null;
        object = context.getBean(id);
        return object;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
}

