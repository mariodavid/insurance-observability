package com.insurance.autoconfigure.account;

import com.insurance.account.AccountConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({AccountConfiguration.class})
public class AccountAutoConfiguration {
}

