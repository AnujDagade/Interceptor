package com.interceptor.config;

import com.interceptor.interceptor.ResponseInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterceptorConfig {

    @Bean
    public FilterRegistrationBean<ResponseInterceptor> responseInterceptorFilter(ResponseInterceptor responseInterceptor) {
        FilterRegistrationBean<ResponseInterceptor> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(responseInterceptor);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}