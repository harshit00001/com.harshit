FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S jobpulse && adduser -S jobpulse -G jobpulse
COPY --from=build /build/target/jobpulse-*.jar app.jar
COPY config ./config
RUN mkdir -p /app/data && chown -R jobpulse:jobpulse /app
USER jobpulse

EXPOSE 8090
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

HEALTHCHECK --interval=30s --timeout=5s --start-period=40s \
    CMD wget -qO- http://localhost:8090/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
