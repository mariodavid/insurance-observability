package com.insurance.quote;

import java.util.Collections;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.insurance.common.CommonConfiguration;
import com.insurance.product.ProductConfiguration;
import com.insurance.quote.policy.PolicyProperties;

import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.ActionsConfiguration;
import io.jmix.flowui.sys.ViewControllersConfiguration;

@Configuration
@EnableConfigurationProperties({PolicyProperties.class})
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {EclipselinkConfiguration.class, FlowuiConfiguration.class, CommonConfiguration.class, ProductConfiguration.class})
@PropertySource(name = "com.insurance.quote", value = "classpath:/com/insurance/quote/module.properties")
public class QuoteConfiguration {

    @Bean("quote_QuoteViewControllers")
    public ViewControllersConfiguration screens(final ApplicationContext applicationContext,
                                                final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ViewControllersConfiguration viewControllers
                = new ViewControllersConfiguration(applicationContext, metadataReaderFactory);
        viewControllers.setBasePackages(Collections.singletonList("com.insurance.quote"));
        return viewControllers;
    }

    @Bean("quote_QuoteActions")
    public ActionsConfiguration actions(final ApplicationContext applicationContext,
                                        final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ActionsConfiguration actions
                = new ActionsConfiguration(applicationContext, metadataReaderFactory);
        actions.setBasePackages(Collections.singletonList("com.insurance.quote"));
        return actions;
    }
}
