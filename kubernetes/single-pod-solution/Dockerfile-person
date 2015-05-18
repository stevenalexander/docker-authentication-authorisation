FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-person/PersonApplication.jar /person/PersonApplication.jar
COPY docker/volume-person/config.yml /person/config.yml

RUN mkdir /log
RUN touch /log/person.log
RUN ln -sf /dev/stdout /log/person.log

EXPOSE 8085

CMD ["java", "-jar", "-Done-jar.silent=true", "-Ddw.authorisationApiUri=http://localhost:8083/api/authorisations", "/person/PersonApplication.jar", "server", "/person/config.yml"]
