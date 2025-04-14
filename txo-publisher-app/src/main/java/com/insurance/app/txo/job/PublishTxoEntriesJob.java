package com.insurance.app.txo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insurance.txo.app.TxoEntryPublisher;
import io.jmix.core.security.Authenticated;

public class PublishTxoEntriesJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(PublishTxoEntriesJob.class);

    private final TxoEntryPublisher txoEntryPublisher;

    public PublishTxoEntriesJob(TxoEntryPublisher txoEntryPublisher) {
        this.txoEntryPublisher = txoEntryPublisher;
    }

    @Override
    @Authenticated
    public void execute(JobExecutionContext context) {
        txoEntryPublisher.publish();
    }
}