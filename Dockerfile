# Step 1: Build stage using Gradle + JDK21
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app

# Copy Gradle config first for better caching
COPY gradlew ./
COPY gradle gradle
COPY gradle.properties ./
COPY settings.gradle.kts ./
COPY build.gradle.kts ./

RUN chmod +x gradlew

# Resolve dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy the full app source
COPY . .

RUN chmod +x gradlew

# Build the fat jar
RUN ./gradlew clean shadowJar --no-daemon

# Step 2: Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the generated fat jar from the build stage
COPY --from=build /app/build/libs/*-all.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
