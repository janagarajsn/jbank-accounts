FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/accounts*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]