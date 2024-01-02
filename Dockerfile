FROM openjdk:11-jre-slim

COPY target/*.jar /app/testapp.jar

WORKDIR /app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "testapp.jar"]