# Multi-stage build for optimized image size
FROM gradle:8.5-jdk17-alpine AS build

WORKDIR /app

# Copy gradle files first (better caching)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies (cached layer)
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build application
RUN gradle clean build -x test --no-daemon

# Runtime stage - usando versión más ligera
FROM eclipse-temurin:17-jre-alpine

# Agregar curl para health checks (más confiable que wget)
RUN apk add --no-cache curl

WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar from build stage
COPY --from=build --chown=spring:spring /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check usando curl
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Optimizaciones de JVM para containers
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]