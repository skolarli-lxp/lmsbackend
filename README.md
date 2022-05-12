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
./graldew clean build

docker build -t lmsservice:latest .  
docker images  
docker run -p 8080:8080 lmsservice:latest
```

