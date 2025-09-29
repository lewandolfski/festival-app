# Multi-stage Dockerfile for Review Service
FROM eclipse-temurin:21-jdk-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks and create non-root user for security
RUN apk add --no-cache curl && \
    addgroup -g 1000 reviewservice && \
    adduser -D -s /bin/sh -u 1000 -G reviewservice reviewservice

# Set working directory
WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/review-service-*.jar app.jar

# Change ownership to non-root user
RUN chown -R reviewservice:reviewservice /app
USER reviewservice

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# JVM optimization for containers
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
