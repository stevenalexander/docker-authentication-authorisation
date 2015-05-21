FROM debian:jessie

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

ENV OPENRESTY_VERSION 1.7.10.1

# OpenResty deps
RUN apt-get update && \
	apt-get -y install libreadline-dev libncurses5-dev libpcre3-dev libssl-dev perl build-essential curl

# Install OpenResty with minimum modules for Lua scripting
RUN curl -O http://openresty.org/download/ngx_openresty-${OPENRESTY_VERSION}.tar.gz && \
	tar xzvf ngx_openresty-${OPENRESTY_VERSION}.tar.gz && \
	cd ngx_openresty-${OPENRESTY_VERSION}/ && \
    ./configure --with-luajit --with-http_gzip_static_module --with-http_ssl_module --with-pcre-jit && \
	make && \
	make install && \
	rm -rf /ngx_openresty*

# forward request and error logs to docker log collector
RUN mkdir /var/log/nginx
RUN touch /var/log/nginx/error.log
RUN mkdir /opt/nginx
RUN mkdir /opt/nginx/conf
RUN mkdir /opt/nginx/conf/logs
RUN touch /opt/nginx/conf/logs/access.log
RUN touch /opt/nginx/conf/logs/error.log
RUN ln -sf /dev/stdout /opt/nginx/conf/logs/access.log
RUN ln -sf /dev/stderr /opt/nginx/conf/logs/error.log

COPY docker/volume-nginx-conf.d/nginx.conf /opt/nginx/conf/nginx.conf
COPY docker/volume-nginx-conf.d/access.lua /opt/nginx/conf/access.lua
COPY docker/volume-nginx-conf.d/authenticate.lua /opt/nginx/conf/authenticate.lua
COPY docker/volume-nginx-conf.d/logout.lua /opt/nginx/conf/logout.lua

EXPOSE 80 443

CMD ["/usr/local/openresty/nginx/sbin/nginx", "-p", "/opt/nginx/conf", "-c", "nginx.conf"]
