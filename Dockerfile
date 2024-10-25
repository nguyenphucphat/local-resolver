# Stage 1: Build the application by maven
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image to deploy
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar ia03-be.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","iao3-be.jar"]
