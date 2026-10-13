# ==============================================================================
# Multi-stage Dockerfile for Capacity Connect Spring Boot Backend
# Optimized for Render deployment with Temurin Java 17 and memory limits
# ==============================================================================

# Stage 1: Build JAR with Maven
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder
WORKDIR /build

# Copy dependency definition and download dependencies for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and package application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Minimal JRE Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Run as non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy built artifact from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Render assigns dynamic PORT environment variable (defaults to 8080)
ENV PORT=8080
EXPOSE 8080

# Tune JVM heap memory for Render free tier (512MB RAM) and bind port dynamically
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-Dserver.port=${PORT}", "-jar", "app.jar"]
