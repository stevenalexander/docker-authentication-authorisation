FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-person/PersonApplication.jar /person/PersonApplication.jar
COPY docker/volume-person/config.yml /person/config.yml

RUN mkdir /log
RUN touch /log/person.log
RUN ln -sf /dev/stdout /log/person.log

EXPOSE 8085

# start Dropwizard application with config overrides
CMD ["java", "-jar", "-Done-jar.silent=true", \
     "-Ddw.database.driverClass=com.mysql.jdbc.Driver", \
     "-Ddw.database.user=root", \
     "-Ddw.database.password=root", \
     "-Ddw.database.url=jdbc:mysql://person-mysql:3306/person", \
     "/person/PersonApplication.jar", "server", "/person/config.yml"]
