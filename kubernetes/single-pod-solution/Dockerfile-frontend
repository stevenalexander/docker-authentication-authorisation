FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-frontend/FrontendApplication.jar /frontend/FrontendApplication.jar
COPY docker/volume-frontend/config.yml /frontend/config.yml

RUN mkdir /log
RUN touch /log/frontend.log
RUN ln -sf /dev/stdout /log/frontend.log

EXPOSE 8081

CMD ["java", "-jar", "-Done-jar.silent=true", "-Ddw.personApiUri=http://localhost:8085/api/persons", "/frontend/FrontendApplication.jar", "server", "/frontend/config.yml"]
