FROM eclipse-temurin:17.0.6_10-jdk-focal
WORKDIR /app
#COPY src/main/resources/db/test.db /app/db/test.db
#COPY ./db /app/db
COPY target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

#docker build -t image_name