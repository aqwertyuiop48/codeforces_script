# ---------- Build Stage ----------
FROM gradle:8.6.0-jdk21 AS build
WORKDIR /app

# Copy configuration and wrapper files first for layer caching
COPY build.gradle.kts settings.gradle.kts gradle.properties ./
COPY gradlew ./
COPY gradle gradle
RUN chmod +x gradlew

# Copy full project source
COPY . .

# Restrict Gradle memory and disable daemon to prevent cloud builder OOM crashes
ENV GRADLE_OPTS="-Xmx1024m -Dorg.gradle.daemon=false"
RUN ./gradlew shadowJar -x test --no-daemon

# Isolate the shadow fat JAR into a predictable path without wildcards
RUN find build/libs -name "*-all.jar" -exec cp {} /app/app.jar \;

# ---------- Run Stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the static, pre-resolved fat JAR
COPY --from=build /app/app.jar app.jar

# Both Render and Back4App inject PORT dynamically
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
