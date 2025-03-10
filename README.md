# Insurance Observability

## Overview

Insurance Observability is an evaluation project that explores different Observability implementations in the Java ecosystem. The goal is to understand how various tools and frameworks—such as OpenTelemetry, Micrometer, and different logging and tracing solutions—can be integrated into insurance-related software architectures.

This project serves as a practical guide for developers and architects who want to compare different Observability approaches, experiment with integrations, and learn best practices for monitoring, logging, and tracing in Java applications.

### Key Topics Covered

* Tracing, Logging, and Metrics: Understanding the differences and use cases
* Comparison of OpenTelemetry and Micrometer Tracing
* Instrumentation strategies: Manual vs. automatic instrumentation
* Backend integrations: Loki, Prometheus, Tempo, Jaeger, Zipkin, and others
* Using OpenTelemetry Java Agent for zero-code instrumentation

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

#### 6. Creating Random Quotes

```shell
watch -n 5 'curl -s -X POST --location http://host.docker.internal:9400/quote/api/quotes/random | jq'
```