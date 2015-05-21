FROM java:8-jre

MAINTAINER NGINX Steven Alexander "steven.william.alexander@googlemail.com"

COPY docker/volume-session/SessionApplication.jar /session/SessionApplication.jar
COPY docker/volume-session/config.yml /session/config.yml

RUN mkdir /log
RUN touch /log/session.log
RUN ln -sf /dev/stdout /log/session.log

EXPOSE 8084

# start Dropwizard application with config overrides
CMD ["java", "-jar", "-Done-jar.silent=true", \
     "-Ddw.database.driverClass=com.mysql.jdbc.Driver", \
     "-Ddw.database.user=root", \
     "-Ddw.database.password=root", \
     "-Ddw.database.url=jdbc:mysql://session-mysql:3306/session", \
     "/session/SessionApplication.jar", "server", "/session/config.yml"]
