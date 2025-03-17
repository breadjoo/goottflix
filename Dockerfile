# 빌드 스테이지 (멀티스테이지 빌드)
FROM gradle:8.5-jdk17-alpine as build

WORKDIR /app

# 소스 복사
COPY --chown=gradle:gradle . .

# 빌드 실행
RUN gradle clean build -x test

# -------------------------------

# 실제 실행 이미지
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 빌드된 app.jar 복사
COPY --from=build /app/build/libs/*.jar ./app.jar

# 포트 오픈
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
