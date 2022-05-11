# lmsservice
Backend service for LMS 

To build and run the docker container 
```
cd lmsservice   
./graldew clean build

docker build -t lmsservice:latest .  
docker images  
docker run -p 8080:8080 lmsservice:latest
```

