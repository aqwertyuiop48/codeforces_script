# Step 1: Build stage using Gradle + JDK21
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app

# Copy Gradle build files first for caching
COPY build.gradle settings.gradle gradle.properties ./
COPY gradlew ./
COPY gradle gradle

# Make gradlew executable
RUN chmod +x gradlew

# Pre-fetch dependencies (fail fast if resolution breaks, so no corrupt cache layer)
RUN ./gradlew dependencies --no-daemon

# Copy full project
COPY . .

# Make gradlew executable again (COPY . . overwrites permissions)
RUN chmod +x gradlew

# Build the application with shadowJar and stage the artifact at a fixed path
RUN ./gradlew shadowJar --no-daemon \
 && cp build/libs/*-all.jar /app/app.jar

# Step 2: Runtime stage using JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy generated fat JAR (exact path, no wildcard — avoids BuildKit walking the Gradle cache)
COPY --from=build /app/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
