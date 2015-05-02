# Dockerfile for nginx web server using OpenResty as a reverse proxy for a microservice solution, handling authentication and authorisation

FROM ubuntu:14.04
MAINTAINER Steven Alexander <steven.william.alexander@googlemail.com>
ENV REFRESHED_AT 2015-04-09

ENV    DEBIAN_FRONTEND noninteractive
RUN    echo "deb-src http://archive.ubuntu.com/ubuntu trusty main" >> /etc/apt/sources.list
RUN    sed 's/main$/main universe/' -i /etc/apt/sources.list
RUN    apt-get update
RUN    apt-get upgrade -y
RUN    apt-get -y install wget vim git libpq-dev

# Openresty (Nginx)
RUN    apt-get -y build-dep nginx \
  && apt-get -q -y clean && rm -rf /var/cache/apt/archives/* /var/lib/apt/lists/*
RUN    wget http://openresty.org/download/ngx_openresty-1.7.10.1.tar.gz \
  && tar xvfz ngx_openresty-1.7.10.1.tar.gz \
  && cd ngx_openresty-1.7.10.1 \
  && ./configure --with-luajit --with-http_ssl_module --with-ipv6 --with-pcre-jit \
  && make \
  && make install \
  && rm -rf /ngx_openresty*

EXPOSE 8080
CMD /usr/local/openresty/nginx/sbin/nginx -p `pwd` -c nginx.conf
