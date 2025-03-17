# Stage 1 - build jar
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# Stage 2 - runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
