package com.insurance.autoconfigure.product;

import com.insurance.product.ProductConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({ProductConfiguration.class})
public class ProductAutoConfiguration {
}

