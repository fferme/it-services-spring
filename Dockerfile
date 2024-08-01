# Etapa de build
FROM maven:3.9-amazoncorretto-21 AS builder

ENV MAVEN_OPTS="-XX:+TieredCompilation -XX:TieredStopAtLevel=1"

LABEL maintainer="fferme"

WORKDIR /opt/demo

COPY pom.xml .
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean install -Dmaven.test.skip=true -X

# --------------------------------------------------------------------------
# Etapa de runtime
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="fferme"

RUN addgroup -S spring-app && adduser -S spring-app -G spring-app

HEALTHCHECK --interval=20s --timeout=5s --start-period=40s --retries=5 \
    CMD curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1

WORKDIR /var/spring-app

COPY --from=builder /opt/demo/target/it-services-spring-0.0.1.jar .

RUN chown spring-app:spring-app it-services-spring-0.0.1.jar
USER spring-app

EXPOSE 8080

ARG VERSION
ENV VERSION=${VERSION:-1.0.0}

CMD ["java", "-jar", "it-services-spring-0.0.1.jar"]
