package com.jaramgroupware.mms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> p.setFallbackPageable(PageRequest.of(0, Integer.MAX_VALUE));
    }
}
