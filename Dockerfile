FROM amazoncorretto:25-al2023
WORKDIR /app
COPY target/liveness-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
