package com.insurance.autoconfigure.quote;

import com.insurance.quote.QuoteConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({QuoteConfiguration.class})
public class QuoteAutoConfiguration {
}

