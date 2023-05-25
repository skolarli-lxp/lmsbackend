FROM openjdk:11
LABEL maintainer="jnair"
ADD build/libs/lmsservice-0.0.1-SNAPSHOT.jar lmsservice.jar

ADD  truststore.jks truststore.jks

ENTRYPOINT ["java", "-Dspring.profiles.active=stg", "-jar", "lmsservice.jar"]
