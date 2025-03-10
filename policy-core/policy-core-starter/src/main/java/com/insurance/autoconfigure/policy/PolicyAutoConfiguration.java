package com.insurance.autoconfigure.policy;

import com.insurance.policy.PolicyConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({PolicyConfiguration.class})
public class PolicyAutoConfiguration {
}

