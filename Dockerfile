FROM adoptopenjdk/openjdk11:alpine-jre
ARG VERSION=0.0.1-SNAPSHOT

EXPOSE 8086

ADD target/eCommercePlatform-${VERSION}.war app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
