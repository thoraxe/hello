FROM thoraxe/jdk8-maven:latest
# above built FROM jboss/base-jdk:8
MAINTAINER Erik Jacobs <erikmjacobs@gmail.com>

ENV JAVA_APP_JAR hello-0.0.1-SNAPSHOT-fat.jar

EXPOSE 8080

USER root
WORKDIR /app/
ADD . /contents
RUN cd /contents && mvn clean package
RUN mv /contents/target/$JAVA_APP_JAR /app \
    && mv /contents/src/openshift/config.json /app \
    && chgrp -R 0 /app \
    && chmod -R g+rw /app

EXPOSE 8080
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $JAVA_APP_JAR -cluster -conf config.json"]
