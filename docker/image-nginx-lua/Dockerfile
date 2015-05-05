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
RUN mkdir /var/log/nginx && touch /var/log/nginx/access.log && touch /var/log/nginx/error.log
RUN ln -sf /dev/stdout /var/log/nginx/access.log
RUN ln -sf /dev/stderr /var/log/nginx/error.log

VOLUME ["/opt/nginx/conf"]

EXPOSE 80 443

CMD ["/usr/local/openresty/nginx/sbin/nginx", "-p", "/opt/nginx/conf", "-c", "nginx.conf"]
