package com.insurance.app.partner.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.api.OpenTelemetry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ObservationConfig {

    @Bean
    public Tracer micrometerTracer(OpenTelemetry openTelemetry) {
        return new OtelTracer(
                openTelemetry.getTracer("partner-app"),
                new OtelCurrentTraceContext(),
                $ -> {} // noop
        );
    }

    @Bean
    public ObservationRegistry observationRegistry(Tracer tracer) {
        ObservationRegistry registry = ObservationRegistry.create();
        registry.observationConfig().observationHandler(new DefaultTracingObservationHandler(tracer));
        return registry;
    }

    @Bean
    public ObservedAspect observationAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }
}