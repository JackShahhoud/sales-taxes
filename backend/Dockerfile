FROM openjdk:15.0-jdk-oracle

WORKDIR /opt/app

COPY /target/Sales-Taxes-0.0.1-SNAPSHOT.jar ./


ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","Sales-Taxes-0.0.1-SNAPSHOT.jar"]