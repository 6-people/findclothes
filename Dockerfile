FROM openjdk:17
ARG JAR_FILE=./build/libs/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]