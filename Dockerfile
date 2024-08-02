FROM openjdk:21-oracle
EXPOSE 8181
RUN mkdir /opt/app
COPY target/task-manager.jar /opt/app
ENTRYPOINT ["java", "-jar", "/opt/app/task-manager.jar"]
