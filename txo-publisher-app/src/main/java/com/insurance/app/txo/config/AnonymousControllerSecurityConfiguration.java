package com.insurance.app.txo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import io.jmix.core.JmixSecurityFilterChainOrder;
import io.jmix.security.util.JmixHttpSecurityUtils;

@Configuration
public class AnonymousControllerSecurityConfiguration {

    @Bean
    @Order(JmixSecurityFilterChainOrder.CUSTOM)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**", "/actuator/**")
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().permitAll() 
                )
                .csrf(AbstractHttpConfigurer::disable);
        JmixHttpSecurityUtils.configureAnonymous(http);
        return http.build();
    }
}