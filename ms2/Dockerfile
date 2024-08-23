FROM eclipse-temurin:17.0.6_10-jdk-focal
WORKDIR /app
COPY target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

#docker build -t image_name