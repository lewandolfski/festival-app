# Festival Application - Simple Docker Build
# This Dockerfile creates a container for the Spring Boot application

# Use Eclipse Temurin JRE for runtime
FROM eclipse-temurin:21-jre-alpine

# Add metadata
LABEL maintainer="Festival Application Team"
LABEL description="Spring Boot REST API for Festival DJ and Performance Management"
LABEL version="1.0.0"

# Install curl for health checks and create non-root user for security
RUN apk add --no-cache curl && \
    addgroup -g 1000 festival && \
    adduser -D -s /bin/sh -u 1000 -G festival festival

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file (build locally first)
COPY target/festival-application-*.jar app.jar

# Change ownership to non-root user
RUN chown festival:festival app.jar

# Switch to non-root user
USER festival

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
