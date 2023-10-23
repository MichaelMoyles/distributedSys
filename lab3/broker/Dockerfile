FROM openjdk:8-jdk-alpine
ENV BROKER_PORT=9000
COPY target/broker-*-allinone.jar /broker.jar
CMD ["java", "-jar", "/broker.jar", "--port", "$BROKER_PORT"]