FROM openjdk:17-alpine

WORKDIR /usr/app/

ARG JAR_FILE=/build/libs/mentoring-1.1.0.jar

COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]