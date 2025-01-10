package com.ofeksag.book_management.config;

import com.ofeksag.book_management.filter.ValidateWithoutBodyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<ValidateWithoutBodyFilter> validateWithoutBodyFilter() {
        FilterRegistrationBean<ValidateWithoutBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ValidateWithoutBodyFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}