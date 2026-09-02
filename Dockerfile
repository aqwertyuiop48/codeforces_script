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

# Restrict Gradle memory/daemon, build fat jar, and stage to a fixed path
ENV GRADLE_OPTS="-Xmx1024m -Dorg.gradle.daemon=false"
RUN ./gradlew clean shadowJar -x test --no-daemon \
 && cp build/libs/*-all.jar /app/app.jar

# Step 2: Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the static, pre-resolved fat jar (no wildcards, preventing Back4App/Render path mismatch)
COPY --from=build /app/app.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
