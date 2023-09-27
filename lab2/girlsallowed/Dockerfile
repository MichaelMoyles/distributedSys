FROM openjdk:19-jdk-alpine
ENV SERVER_HOST=localhost
COPY target/girlsallowed-*-allinone.jar /girlsallowed.jar
CMD  sleep 5 && /opt/openjdk-19/bin/java -jar /girlsallowed.jar $SERVER_HOST
