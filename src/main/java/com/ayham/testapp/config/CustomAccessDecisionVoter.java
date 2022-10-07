package com.ayham.testapp.config;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.ayham.testapp.service.UserService;
import com.ayham.testapp.util.ApplicationContextUtils;
import java.util.Collection;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

public class CustomAccessDecisionVoter implements AccessDecisionVoter {

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        FilterInvocation invocation = (FilterInvocation) object;
        String url = invocation.getRequestUrl();
        if (url.endsWith("/register") || url.endsWith("/login") || url.contains("swagger-ui")) {
            return ACCESS_GRANTED;
        }
        try {
            String token = invocation.getHttpRequest().getHeaders(AUTHORIZATION).nextElement();
            UserService userService = ApplicationContextUtils.getUserService();

            return userService.isTokenValid(token) ? ACCESS_GRANTED : ACCESS_DENIED;
        } catch (Exception e) {
            return ACCESS_DENIED;
        }
    }

    @Override
    public boolean supports(Class clazz) {
        return true;
    }
}
