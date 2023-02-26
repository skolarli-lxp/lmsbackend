# lmsservice
Backend service for LMS 

### Prerequisites 
MYSQL db running with the following specs  
```
Server: localhost:3306
Schema Name: lms
Username: lmsuser
Password: <protected>
```

### To build and run the docker container 
```
cd lmsservice   
./gradlew clean build

docker build -t lmsservice:latest .  
docker images  
docker run -p 8080:8080 lmsservice:latest
```

### To push docker image to Digital Ocean Docker Registry
```
doctl registry login 
docker tag lmsservice:latest skolarlistgregistry.azurecr.io/lmsservice:latest
docker push skolarlistgregistry.azurecr.io/lmsservice:latest
```
<!--
docker tag lmsservice:latest registry.digitalocean.com/skolarli/lmssservice:latest
docker push registry.digitalocean.com/skolarli/lmssservice:latest
-->

