#!/bin/bash

export OTEL_SERVICE_NAME=policy-app
export OTEL_LOGS_EXPORTER=otlp
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_JAVAAGENT_DEBUG=false
export OTEL_INSTRUMENTATION_EXCLUDE_CLASSES=io.jmix.flowui.backgroundtask.BackgroundTaskWatchDogScheduleConfigurer*,io.jmix.core.impl.TriggerFileProcessorScheduleConfigurer*
export OTEL_INSTRUMENTATION_LOGBACK_MDC_ENABLED=true

if [ ! -f opentelemetry-javaagent.jar ] ; then
    curl -OL https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.13.3/opentelemetry-javaagent.jar
fi

rm -rf ./policy-app/build/libs/

./gradlew :policy-core:policy-core:clean
./gradlew :policy-app:clean
./gradlew -Pvaadin.productionMode=true :policy-app:bootJar --no-build-cache


java -javaagent:$(pwd)/opentelemetry-javaagent.jar -jar ./policy-app/build/libs/policy-app-0.0.1-SNAPSHOT.jar "$@"