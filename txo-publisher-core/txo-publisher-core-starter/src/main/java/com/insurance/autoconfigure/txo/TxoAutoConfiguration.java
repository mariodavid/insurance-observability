package com.insurance.autoconfigure.txo;

import com.insurance.txo.TxoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import({TxoConfiguration.class})
public class TxoAutoConfiguration {
}

