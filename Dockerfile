# Stage 1: Sử dụng Maven để build ứng dụng
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng và bỏ qua test (nếu không cần chạy test trong build)
RUN mvn clean package -DskipTests

# Stage 2: Sử dụng OpenJDK để chạy ứng dụng
FROM openjdk:21-jdk-slim

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ stage 1 vào container
COPY --from=build /app/target/ManagementBE-0.0.1-SNAPSHOT.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
