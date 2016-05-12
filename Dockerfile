FROM java:8-jre-alpine

ENV JAVA_APP_JAR hello-0.0.1-SNAPSHOT-fat.jar

EXPOSE 8080

ADD target/$JAVA_APP_JAR /app/
ADD src/openshift/config.json /app/
WORKDIR /app/
ENTRYPOINT ["sh", "-c"]
CMD ["java -jar $JAVA_APP_JAR -cluster -conf config.json"]
