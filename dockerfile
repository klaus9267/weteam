FROM openjdk:17.0.2-jdk

ENV APP_HOME=/apps

ARG JAR_FILE_PATH=build/libs/backend-0.0.1-SNAPSHOT.jar
ARG FIREBASE_JSON_PATH=./src/main/resources/firebase.json

WORKDIR $APP_HOME

COPY $FIREBASE_JSON_PATH .
COPY $JAR_FILE_PATH app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]