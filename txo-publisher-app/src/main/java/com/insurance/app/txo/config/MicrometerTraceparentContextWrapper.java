package com.insurance.app.txo.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.insurance.txo.app.TraceparentContextWrapper;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;

@Component
public class MicrometerTraceparentContextWrapper implements TraceparentContextWrapper {


    private static final Logger log = LoggerFactory.getLogger(MicrometerTraceparentContextWrapper.class);
    private final Tracer tracer;

    public MicrometerTraceparentContextWrapper(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void runInContext(String traceparent, Runnable runnable) {
        Map<String, String> carrier = new HashMap<>();
        carrier.put("traceparent", traceparent);

        String[] split = traceparent.split("-");
        TraceContext parentContext = tracer.traceContextBuilder()
                .traceId(split[1])
                .spanId(split[2])
                .sampled(true)
                .build();

        log.info("parent context: traceId={}, spanId={}", parentContext.traceId(), parentContext.spanId());

        Span span = tracer.spanBuilder()
                        .setParent(parentContext)
                        .name("txo-publish")
                        .start();

        try (var scope = tracer.withSpan(span)) {
            runnable.run();
        } finally {
            span.end();
        }
    }
}