# Introduction
Java Version: 1.8.0_202

This is the basic implementation of the Life Insurance Multi Module Quotation Service. Each service was converted to a 
Docker Image and run from a Docker Compose file in the root directory. For this implementation, Representational State Transfer (REST) is used for the interaction between each of the 3 quotation services and the broker and between the broker and the client. The Spring Boot framework is used to implement the system.

Docker images are used for each of the services, it is implemented only for the links between the broker and the 
quotation services. The services and the broker were converted to docker images, with a Docker file contained within 
each service and a docker compose file in the root directory to coordinate the build process. Each service connects 
to the broker service which the client then connects to manually. On execution of Spring Boot, a registration service 
has been integrated into the broker. This service exposes the list of URLs used by the broker to contact quotation 
services at the /services endpoint.

### Running the Program (From Root Directory)

1.  Use the command shell go to the task5 folder, where the pom.xml file is 
2.  To install the maven modules, run: `mvn clean install`
3.  Create a docker image for each service using: `docker-compose build`
4.  Run the containers: `docker-compose up`
5.  In a new command shell, run the client service, this can take a few minutes to run: `mvn spring-boot:run -pl client`