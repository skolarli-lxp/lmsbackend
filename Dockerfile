FROM openjdk:11
LABEL maintainer="jnair"
ADD build/libs/lmsservice-0.0.1-SNAPSHOT.jar lmsservice.jar
ENTRYPOINT ["java", "-jar", "lmsservice.jar"]

# Comamands to build and run
# docker build -t lmsservice:latest .
# docker images
# docker run -p 8080:8080 lmsservice:latest