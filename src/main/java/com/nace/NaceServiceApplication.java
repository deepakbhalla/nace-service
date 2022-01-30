package com.nace;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.nace.filters.CorrelationHeaderFilter;

/**
 * Nace Service main application.
 * 
 * @author Deepak Bhalla
 *
 */
@SpringBootApplication
@EnableCaching
public class NaceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaceServiceApplication.class, args);
    }

    /**
     * A convenient BeanPostProcessor implementation provider for performing
     * method-level validation on annotated methods.
     * 
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * Register CorrelationHeaderFilter as FilterRegistrationBean.
     */
    @Bean
    public FilterRegistrationBean correlationHeaderFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
        filterRegBean.setFilter(new CorrelationHeaderFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));

        return filterRegBean;
    }
}
