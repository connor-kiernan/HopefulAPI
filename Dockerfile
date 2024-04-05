FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY src /home/app/hapi/src
COPY pom.xml /home/app/hapi
WORKDIR /home/app/hapi
RUN --mount=type=cache,target=/root/.m2 mvn clean package

FROM amazoncorretto:21
COPY --from=build /home/app/hapi/target/HopefulAPI-0-SNAPSHOT.jar app.jar
ENTRYPOINT java -jar app.jar