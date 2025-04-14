package com.insurance.app.txo.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.insurance.txo.app.TraceparentSupplier;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;

@Component
public class MicrometerTraceparentSupplier implements TraceparentSupplier {

    private final Tracer tracer;
    private final Propagator propagator;

    public MicrometerTraceparentSupplier(Tracer tracer, Propagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @Override
    public String get() {
        Map<String, String> carrier = new HashMap<>();
        propagator.inject(tracer.currentTraceContext().context(), carrier, Map::put);
        return carrier.get("traceparent");
    }
}