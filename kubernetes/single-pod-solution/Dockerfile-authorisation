FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-authorisation/AuthorisationApplication.jar /authorisation/AuthorisationApplication.jar
COPY docker/volume-authorisation/config.yml /authorisation/config.yml

RUN mkdir /log
RUN touch /log/authorisation.log
RUN ln -sf /dev/stdout /log/authorisation.log

EXPOSE 8083

CMD ["java", "-jar", "-Done-jar.silent=true", "/authorisation/AuthorisationApplication.jar", "server", "/authorisation/config.yml"]
