FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-session/SessionApplication.jar /session/SessionApplication.jar
COPY docker/volume-session/config.yml /session/config.yml

RUN mkdir /log
RUN touch /log/session.log
RUN ln -sf /dev/stdout /log/session.log

EXPOSE 8084

CMD ["java", "-jar", "-Done-jar.silent=true", "/session/SessionApplication.jar", "server", "/session/config.yml"]
