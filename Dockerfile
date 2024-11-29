FROM openjdk:17-jdk-slim
COPY build/libs/*.jar app.jar
ENV SERVER_PORT=${SERVER_PORT}
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-jar","/app.jar"]
