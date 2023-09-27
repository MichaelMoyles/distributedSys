FROM openjdk:19-jdk-alpine
ENV SERVER_HOST=localhost
COPY target/auldfellas-*-allinone.jar /auldfellas.jar
CMD sleep 5 && /opt/openjdk-19/bin/java -jar /auldfellas.jar $SERVER_HOST
