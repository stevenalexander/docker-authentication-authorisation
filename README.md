# Docker authentication and authorisation images

Example solution using Docker to create a number of containers which together implement a microservices based web application with authentication and authorisation.

To run nginx-lua:

```
# Build
docker build -t="example/nginx-lua" nginx-lua/Dockerfile

# Run
docker run -t -i -P -v=`pwd`/nginx-lua:/conf -w=/conf example/nginx-lua
```
