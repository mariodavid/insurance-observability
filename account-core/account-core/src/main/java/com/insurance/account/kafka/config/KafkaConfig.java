package com.insurance.account.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.retrytopic.RetryTopicConfiguration;
import org.springframework.kafka.retrytopic.RetryTopicConfigurationBuilder;

@Configuration
@EnableKafka
public class KafkaConfig {


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);

        factory.getContainerProperties().setObservationEnabled(true);

        return factory;
    }

    @Bean
    public RetryTopicConfiguration retryTopicConfiguration(
            KafkaTemplate<String, String> template) {
        return RetryTopicConfigurationBuilder
                .newInstance()
                .maxAttempts(3)
                .exponentialBackoff(1000, 2, 30000)
                .retryTopicSuffix("-retry")
                .dltSuffix("-dlt")
                .autoStartDltHandler(false)
                .create(template);
    }

}
