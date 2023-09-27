FROM openjdk:19-jdk-alpine
COPY target/broker-*-allinone.jar /broker.jar
CMD sleep 5 && /opt/openjdk-19/bin/java -jar /broker.jar
