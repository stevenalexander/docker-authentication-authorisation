# Docker OpenResty

Example Dockerfile for building an [nginx](http://wiki.nginx.org/Main) image with [OpenResty](http://openresty.org). Based off the original [docker/nginx](https://github.com/dockerfile/nginx) image.

## Run

```
# build image
docker build -t stevena/openresty:v1 .

# run container (requires nginx configuration file in ./conf/nginx.conf)
docker run -t -i -p 80:8080 -v=`pwd`/conf:/opt/nginx/conf -w=/opt/nginx/conf stevena/openresty:v1
```
