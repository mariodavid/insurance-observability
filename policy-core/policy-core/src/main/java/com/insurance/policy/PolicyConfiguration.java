package com.insurance.policy;

import java.util.Collections;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.insurance.common.CommonConfiguration;
import com.insurance.product.ProductConfiguration;

import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.eclipselink.EclipselinkConfiguration;
import io.jmix.flowui.FlowuiConfiguration;
import io.jmix.flowui.sys.ActionsConfiguration;
import io.jmix.flowui.sys.ViewControllersConfiguration;

@Configuration
@ComponentScan
@ConfigurationPropertiesScan
@JmixModule(dependsOn = {EclipselinkConfiguration.class, FlowuiConfiguration.class, CommonConfiguration.class, ProductConfiguration.class})
@PropertySource(name = "com.insurance.policy", value = "classpath:/com/insurance/policy/module.properties")
public class PolicyConfiguration {

    @Bean("policy_PolicyViewControllers")
    public ViewControllersConfiguration screens(final ApplicationContext applicationContext,
                                                final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ViewControllersConfiguration viewControllers
                = new ViewControllersConfiguration(applicationContext, metadataReaderFactory);
        viewControllers.setBasePackages(Collections.singletonList("com.insurance.policy"));
        return viewControllers;
    }

    @Bean("policy_PolicyActions")
    public ActionsConfiguration actions(final ApplicationContext applicationContext,
                                        final AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        final ActionsConfiguration actions
                = new ActionsConfiguration(applicationContext, metadataReaderFactory);
        actions.setBasePackages(Collections.singletonList("com.insurance.policy"));
        return actions;
    }
}
