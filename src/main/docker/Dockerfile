FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} Resource-Tracker-0.0.1-SNAPSHOT.jar
#ADD target/Resource-Tracker-0.0.1-SNAPSHOT.jar target/Resource-Tracker-0.0.1-SNAPSHOT.jar
#RUN bash -c 'touch target/Resource-Tracker-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","Resource-Tracker-0.0.1-SNAPSHOT.jar"]
