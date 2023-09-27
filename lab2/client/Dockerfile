FROM openjdk:19-jdk-alpine
ENV SERVER_HOST=localhost
COPY target/client-*-allinone.jar /client.jar
CMD sleep 5 && /opt/openjdk-19/bin/java -jar /client.jar $SERVER_HOST
