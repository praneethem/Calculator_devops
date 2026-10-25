# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy Maven configuration first to improve dependency caching
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Copy application source and package the executable JAR
COPY src src
RUN ./mvnw -B clean package -DskipTests

# Stage 2: Run the application with a smaller JRE image
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Run as a non-root user
RUN useradd --system --uid 10001 --create-home appuser

COPY --from=builder /app/target/calculator.jar /app/calculator.jar

USER 10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/calculator.jar"]
