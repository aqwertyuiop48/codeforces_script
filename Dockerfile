# Step 1: Build stage using Gradle + JDK21
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app

# Copy Gradle build files and wrapper first for caching
COPY build.gradle settings.gradle gradle.properties ./
COPY gradlew ./
COPY gradle gradle

# Make gradlew executable before running any commands
RUN chmod +x gradlew

# Copy full project source
COPY . .

# Ensure gradlew retains execution permissions after copying all files
RUN chmod +x gradlew

# Restrict Gradle memory, disable daemon, build shadowJar, and copy to fixed path
ENV GRADLE_OPTS="-Xmx1024m -Dorg.gradle.daemon=false"
RUN ./gradlew shadowJar -x test --no-daemon \
 && cp build/libs/*-all.jar /app/app.jar

# Step 2: Runtime stage using JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy generated fat JAR (exact path, no wildcard)
COPY --from=build /app/app.jar app.jar

ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
