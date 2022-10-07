package com.ayham.testapp.util;

import com.ayham.testapp.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    private static final String USER_SERVICE = "userServiceImpl";

    @Override
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        ctx = appContext;
    }

    public static ApplicationContext getApplicationContext() {
        return ctx;
    }

    public static UserService getUserService() {
        return (UserService) ctx.getBean(USER_SERVICE);
    }
}
