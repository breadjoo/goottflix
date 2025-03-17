# 베이스 이미지 설정
FROM eclipse-temurin:17-jdk-jammy as build

WORKDIR /app

# 빌드된 JAR 파일만 복사 (CI/CD에서 빌드 후 복사한다고 가정)
COPY build/libs/*.jar ./app.jar

# 컨테이너에서 8080 포트 오픈
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]
