# Step 1: Build stage using Gradle + JDK21
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app

# Copy Gradle build files first for caching
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradlew ./
COPY gradle gradle

# Make gradlew executable AFTER copying it
RUN chmod +x gradlew

# Download dependencies (skip error if not all appear)
RUN ./gradlew dependencies --no-daemon || true

# Copy full project
COPY . .

# Ensure gradlew is still executable after copying everything
RUN chmod +x gradlew

# Build the application with shadowJar
RUN ./gradlew shadowJar --no-daemon

# Step 2: Runtime stage using JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy generated fat JAR
COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]