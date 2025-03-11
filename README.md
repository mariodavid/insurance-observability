# Insurance Observability

## Overview

Insurance Observability is an evaluation project that explores different Observability implementations in the Java ecosystem. The goal is to understand how various tools and frameworks—such as OpenTelemetry, Micrometer, and different logging and tracing solutions—can be integrated into insurance-related software architectures.

This project serves as a practical guide for developers and architects who want to compare different Observability approaches, experiment with integrations, and learn best practices for monitoring, logging, and tracing in Java applications.

### Application Flow

1. The Quote App allows users to create insurance quotes. Once the quote is accepted by the customer, the application sends an HTTP request to the Policy App, which issues the corresponding policy. 
2. If the policy creation is successful, a message is sent to the "policy-created" Kafka topic. 
3. The Account App listens for this event and generates booking records. 

### Key Topics Covered

* Tracing, Logging, and Metrics: Understanding the differences and use cases
* Comparison of OpenTelemetry and Micrometer Tracing
* Instrumentation strategies: Manual vs. automatic instrumentation
* Backend integrations: Loki, Prometheus, Tempo, Jaeger, Zipkin, and others
* Using OpenTelemetry Java Agent for zero-code instrumentation

### Observability Stack

In this example we use the Grafana Stack for observability, which includes:

- Loki for log aggregation
- Prometheus for metrics collection
- Tempo for distributed tracing

Grafana provides a unified dashboard to visualize logs, metrics, and traces, enabling efficient monitoring and troubleshooting.

This observability stack is just an example. By using the OpenTelemetry Collector, we gain the flexibility to send telemetry data to any backend that supports the OTLP protocol, including services like Datadog, Splunk, Honeycomb, and many others. See also: https://opentelemetry.io/ecosystem/vendors/.

#### Open Telemetry Collector

All telemetry data flows through the OpenTelemetry Collector, which acts as an intermediary between applications and the backend observability systems.

- Logs from applications are sent to Loki.
- Metrics are collected using Prometheus.
- Traces are processed and stored in Tempo.

Alternatively, applications can send telemetry data directly to the observability backend without using the OpenTelemetry Collector, depending on the specific requirements and infrastructure.

### Getting Started

#### Prerequisites

* Java 21+
* Docker and Docker Compose (for running backend services)

#### Running the Project

#### 0. host.docker.internal

Ensure you have `host.docker.internal` pointed to 127.0.0.1:

```shell
echo "127.0.0.1 host.docker.internal" | sudo tee -a /etc/hosts
```

#### 1. Docker containers

Spinning up the observability docker containers:

```shell
docker compose -f docker/docker-compose-infra.yaml -f docker/docker-compose-observability.yaml up -d
```

#### 2. Build application

```shell
./build.sh
```

#### 3. Start application

```shell
docker compose -f docker/docker-compose-apps.yaml up --build -d
```

#### 4. Open Apps

```shell
open http://host.docker.internal:9400/quote http://host.docker.internal:9401/policy http://host.docker.internal:9402/account
```


#### 5. Open Grafana

```shell
open http://host.docker.internal:3000
```

* Username: `admin`
* Password: `admin`

#### 6. Creating Random Quotes

```shell
watch -n 5 'curl -s -X POST --location http://host.docker.internal:9400/quote/api/quotes/random | jq'
```

## Quote App

The Quote App consists of two modules: `quote-core` and `quote-app`.  
- `quote-core` represents the core product, containing the business logic while minimizing infrastructural dependencies.  
- `quote-app` is the actual application that integrates `quote-core` and handles infrastructural concerns and OpenTelemetry instrumentation.

In the Quote App, we use the [OpenTelemetry Spring Boot Starter](https://opentelemetry.io/docs/zero-code/java/spring-boot-starter/) to configure automatic OpenTelemetry instrumentation. This allows us to enable tracing without requiring the OpenTelemetry Java Agent.

#### Automatic Instrumentation
We include the following dependency in the application:

```gradle
implementation 'io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter:2.13.3'
```

This provides automatic instrumentation for:
- Outgoing HTTP requests
- JDBC calls

By including this dependency in the target application, OpenTelemetry traces are automatically captured and sent to the configured backend.

#### OpenTelemetry Spring Boot Starter Features

The OpenTelemetry Spring Boot Starter includes support for:
- Traces: Automatic and manual span creation.
- Metrics: Capturing system and application metrics.
- Logging: Integration with Logback, adding a special OpenTelemetry appender.

The starter automatically configures Logback and enriches logs with tracing context, ensuring that traces, metrics, and logs are all correlated within the observability backend.

#### Disabling Specific Auto Instrumentations

In some cases, you might want to disable certain automatic instrumentations provided by the OpenTelemetry Spring Boot Starter. This can be done using application properties. For example, to disable JDBC instrumentation:

```properties
otel.instrumentation.jdbc.enabled=false
```

This allows for more fine-grained control over what is instrumented automatically.

#### Manual Custom Spans
Additionally, in `quote-core`, we include the following (optional) dependency:

```gradle
implementation 'io.opentelemetry.instrumentation:opentelemetry-instrumentation-annotations:1.27.0'
```

Using this dependency, we can annotate specific methods with `@WithSpan` to create custom spans, providing more granular tracing where needed.

##### Example: Creating a Custom Span

```java
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    @WithSpan("Accept Quote") // [optional] creating a custom span for better visibility
    public Quote accept(Id<Quote> quoteId) {
        // ...
    }
}
```

see: [quote-core: Quote Service](quote-core/quote-core/src/main/java/com/insurance/quote/app/QuoteService.java)

Here, the `@WithSpan` annotation ensures that the `generateQuote` method is traced with its own span, making it easier to track its execution within OpenTelemetry.

This approach is particularly useful for tracing key business logic operations that are not automatically captured by the Spring Boot Starter’s default instrumentation.

##### Example: Creating a Span Programmatically

In some cases, such as when using frameworks like Vaadin, automatic instrumentation might not capture relevant spans properly. In such scenarios, spans can be created programmatically using OpenTelemetry's `Tracer` API.

```java
@Subscribe("quotesDataGrid.acceptAction")
public void onQuotesDataGridAcceptAction(final ActionPerformedEvent event) {
    
    Tracer tracer = openTelemetry.getTracer(QuoteListView.class.getName());
    Span span = tracer.spanBuilder("UI: accept quote action").startSpan();
    
    try {
        // ...
    }
    finally {
        span.end();
    }
}
```

see: [quote-core: Quote List View](quote-core/quote-core/src/main/java/com/insurance/quote/view/quote/QuoteListView.java)

This approach ensures that UI-triggered interactions, such as accepting a quote, are properly traced and visible in OpenTelemetry.


### Policy App

The Policy App is instrumented using the OpenTelemetry Java Agent, which allows automatic instrumentation without requiring dependencies in the source code. Instead, the agent is attached at runtime and modifies the bytecode dynamically. The OpenTelemetry Java Agent performs bytecode manipulation to inject instrumentation.

The agent automatically captures:

* Logs
* Metrics
* Traces

#### Configuration
To use the agent, the application needs to be started with the `-javaagent` flag:

```shell
CMD java -javaagent:/opentelemetry-javaagent.jar -jar /application.jar "$@"
```
see: [policy-app: Dockerfile](policy-app/Dockerfile)


Additionally, environment variables configure the behavior:

```shell
OTEL_SERVICE_NAME: policy-app
OTEL_LOGS_EXPORTER: otlp
OTEL_EXPORTER_OTLP_ENDPOINT: http://host.docker.internal:4318
OTEL_JAVAAGENT_DEBUG: false
OTEL_INSTRUMENTATION_LOGBACK_MDC_ENABLED: true
```
see: [docker/docker-compose-apps.yaml](docker/docker-compose-apps.yaml)

### Limitations of the OpenTelemetry Java Agent

While the OpenTelemetry Java Agent provides full automatic instrumentation, it does not support custom instrumentation out of the box. If you need to create custom spans, capture additional metadata, or manually instrument specific business logic, you must include the OpenTelemetry SDK as a dependency in the application.

## Account App

The Account App uses Micrometer and Micrometer Tracing, which are built into Spring Boot Actuator. Instead of relying on OpenTelemetry's Java Agent or Spring Boot Starter, this approach uses Micrometer as the abstraction layer for tracing and metrics.

> Micrometer provides a facade for the most popular observability systems, allowing you to instrument your JVM-based application code without vendor lock-in. Think SLF4J, but for observability.

see: [Micrometer Website](https://micrometer.io/).

### How It Works
- Micrometer handles both metrics and tracing.
- Logs require manual configuration via Logback, as Micrometer does not handle logging.
- A bridge is required to convert Micrometer Tracing data to OpenTelemetry’s OTLP protocol.

### Dependencies
The following dependencies are used in the Account App:

#### Actuator & Micrometer Core
Spring Boot Actuator provides built-in endpoints (e.g., `/actuator/health`, `/actuator/metrics`) and includes Micrometer Core & Micrometer Observation:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

#### Distributed Tracing
To enable distributed tracing via Micrometer and export traces to OpenTelemetry, the following dependencies are included:

```gradle
// Micrometer Tracing Bridge for OTEL
implementation 'io.micrometer:micrometer-tracing-bridge-otel'

// OTLP Exporter to send traces to OpenTelemetry Collector
implementation 'io.opentelemetry:opentelemetry-exporter-otlp'

// Required for @Observed annotation to activate tracing on specific methods
implementation 'org.springframework.boot:spring-boot-starter-aop'
```

#### Logging (Logback)
Since Micrometer does not handle logs, OpenTelemetry's Logback Appender is used to export logs to the OpenTelemetry Collector:

```gradle
implementation 'io.opentelemetry.instrumentation:opentelemetry-logback-appender-1.0:1.32.1-alpha'
```

To properly integrate OpenTelemetry with Logback, a custom `logback.xml` configuration is required. This ensures that logs are both printed to the console and sent to the OpenTelemetry Collector with trace context.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="OpenTelemetry" class="io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender">
        <captureMdcAttributes>*</captureMdcAttributes>
    </appender>
    <root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="OpenTelemetry"/>
    </root>
</configuration>
```

This ensures that logs include tracing metadata such as `trace_id` and `span_id`.

#### Metrics Export
To export Micrometer metrics (counters, gauges, timers, etc.) to OpenTelemetry, the Micrometer OTLP Registry is used:

```gradle
implementation 'io.micrometer:micrometer-registry-otlp'
```

### Key Considerations
- Micrometer only supports tracing and metrics, so log integration requires manual setup.
- A bridge is necessary to convert Micrometer’s tracing data to OpenTelemetry’s OTLP format.
- Unlike the OpenTelemetry Java Agent, this approach allows more control over what is instrumented but requires additional configuration.

This approach is useful for applications that already use Micrometer and want to integrate with OpenTelemetry without fully switching to OpenTelemetry's SDK or Java Agent.
