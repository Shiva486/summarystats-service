FROM openjdk:8-jdk-alpine
MAINTAINER shiva.chandra
COPY target/summarystats-service-0.0.1-SNAPSHOT.jar summarystats-service.jar
ENTRYPOINT ["java","-jar","/summarystats-service.jar"]
