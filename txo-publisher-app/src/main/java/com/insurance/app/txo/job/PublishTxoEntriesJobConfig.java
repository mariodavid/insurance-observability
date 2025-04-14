package com.insurance.app.txo.job;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublishTxoEntriesJobConfig {

    @Bean
    public JobDetail publishTxoEntriesJobDetail() {
        return JobBuilder.newJob(PublishTxoEntriesJob.class)
                .withIdentity("publishTxoEntriesJob")
                .storeDurably()
            .build();
    }

    @Bean
    public Trigger publishTxoEntriesTrigger() {
        return TriggerBuilder.newTrigger()
            .forJob(publishTxoEntriesJobDetail())
            .withIdentity("publishTxoEntriesTrigger")
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)
                .repeatForever())
            .build();
    }
}