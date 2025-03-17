package com.insurance.app.partner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.insurance.partner.config.PartnerCoreConfiguration;

@SpringBootApplication(scanBasePackages = {"com.insurance.app.partner", "com.insurance.partner"})
@Import(PartnerCoreConfiguration.class)
public class PartnerAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartnerAppApplication.class, args);
    }

}
