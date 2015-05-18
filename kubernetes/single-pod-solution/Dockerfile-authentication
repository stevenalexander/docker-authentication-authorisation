FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-authentication/AuthenticationApplication.jar /authentication/AuthenticationApplication.jar
COPY docker/volume-authentication/config.yml /authentication/config.yml

RUN mkdir /log
RUN touch /log/authentication.log
RUN ln -sf /dev/stdout /log/authentication.log

EXPOSE 8082

CMD ["java", "-jar", "-Done-jar.silent=true", "/authentication/AuthenticationApplication.jar", "server", "/authentication/config.yml"]
