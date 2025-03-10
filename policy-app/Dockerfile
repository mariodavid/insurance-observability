FROM eclipse-temurin:21

RUN curl -L -o /opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.13.3/opentelemetry-javaagent.jar

COPY build/libs/policy-app-0.0.1-SNAPSHOT.jar /application.jar

CMD java -javaagent:/opentelemetry-javaagent.jar -jar /application.jar "$@"