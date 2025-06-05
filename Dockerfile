# FROM openjdk:17-jdk-slim
#
# WORKDIR /app
#
# COPY target/*.jar app.jar
#
# EXPOSE 8080
#
# ENTRYPOINT ["java", "-jar", "app.jar"]
#

FROM openjdk:17-jdk-slim

COPY target/project_tracker-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]