FROM openjdk:15.0-jdk-oracle

WORKDIR /opt/app

COPY . .

RUN ./mvnw package

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","target/Sales-Taxes-0.0.1-SNAPSHOT.jar"]