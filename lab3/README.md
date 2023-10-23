# Introduction
Java Version: 1.8.0_202

This is the basic implementation of the Life Insurance Multi Module Quotation Service. Each service was converted to a 
Docker Image and run from a Docker Compose file in the root directory.

A jmDNS based service discovery mechanism is used in this lab. This is implemented only for the links between the broker 
and the quotation services and happens in the Docker Files. The services and the broker were converted to Docker Images, with a Docker file contained within each service and a docker compose file in the root directory to coordinate the build process. Each service connects to the broker service which the client then connects to manually.

### Running the Program (From Root Directory)

1.  Use a command shell, and go to the root folder, where the pom.xml file is
2.  To install the core module, run: `mvn clean install`
3.  To package all maven modules, run: `mvn package`
4.  Create the docker image in the root directory: `docker-compose build`
5.  Run the containers: `docker-compose up`
6.  In a new command shell, run the client service: `mvn exec:java -pl client`