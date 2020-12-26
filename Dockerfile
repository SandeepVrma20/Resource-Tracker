FROM openjdk:8-jdk-alpine
VOLUME /tmp

ARG JAR_FILE=target/*.jar

RUN addgroup -S spring \
   && adduser -S spring -G spring

USER spring:spring

COPY ${JAR_FILE} Resource-Tracker-0.0.1-SNAPSHOT.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/Resource-Tracker-0.0.1-SNAPSHOT.jar"]
