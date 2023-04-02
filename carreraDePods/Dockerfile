FROM openjdk:17-jdk-slim
LABEL maintainer="example@example.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/carreraDePods-1.0.0.jar
ADD ${JAR_FILE} carreraDePods.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/carreraDePods.jar"]