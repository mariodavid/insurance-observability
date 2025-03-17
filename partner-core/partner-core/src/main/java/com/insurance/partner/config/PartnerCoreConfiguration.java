package com.insurance.partner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(basePackages = "com.insurance.partner.repository")
public class PartnerCoreConfiguration {
}