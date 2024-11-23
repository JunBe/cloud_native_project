# 1. Java 21 JDK 설치
FROM eclipse-temurin:21-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 및 프로젝트 파일 복사
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/
COPY src /app/src

# 4. Gradle Wrapper 실행 권한 부여
RUN chmod +x /app/gradlew

# 5. Gradle 빌드
RUN ./gradlew bootJar --no-daemon

# 6. 생성된 JAR 파일 실행
CMD ["java", "-jar", "build/libs/budgettracker-0.0.1-SNAPSHOT.jar"]
