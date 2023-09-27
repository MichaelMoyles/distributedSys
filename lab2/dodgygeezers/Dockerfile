FROM openjdk:19-jdk-alpine
ENV SERVER_HOST=localhost
COPY target/dodgygeezers-*-allinone.jar /dodgygeezers.jar
CMD sleep 5 && /opt/openjdk-19/bin/java -jar /dodgygeezers.jar $SERVER_HOST
