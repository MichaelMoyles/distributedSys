# Introduction
Java Version: 1.8.0_202

This is the basic implementation of the Life Insurance Multi Module Quotation Service. Each service was converted to a 
Docker Image and run from a Docker Compose file in the root directory. Apache ActiveMQ is being used as an enabling 
layer between the services, broker and client.

Docker images are used for each of the services, it is implemented only for the links between the broker and the 
quotation services. The services and the broker were converted to docker images, with a Docker file contained within 
each service and a docker compose file in the root directory to coordinate the build process. Each service 
connects to the broker service which the client then connects to manually.

The docker image created by the docker-compose file in the lab4 directory uses the dockerfiles within each of the 
services. There is an issue with this as if the packaging is done, then the docker image created, the unit tests do not 
run because there are two instances running of each service and trying to utilise ActiveMQ. To get around this, we
first create an image with just ActiveMQ without the quotation services, from the lab4/testDocker directory. Then we delete 
this image and use the docker-compose file in the lab4 directory which will now use all the packaged modules to create
an image for each service. Finally, we run the client against this.

### Running the Program (From Root Directory)

1.  Use a command shell, and go to the lab4/testDocker folder
2.  In order to run the below commands, activate the ActiveMQ service using: `docker-compose up` 
3.  Use a new command shell, and go to the lab4 folder, where the pom.xml file is 
4.  To install the maven modules and unit tests, run: `mvn clean install`
5.  To package all maven modules, run: `mvn package`
6.  Then close the running process in the testDocker folder and run: `docker compose down`
7.  Navigate back to the lab4 root folder and create a docker image for each service using: `docker-compose build`
8.  Run the containers: `docker-compose up`
9.  In a new command shell, run the client service, this can take a few minutes to run: `mvn exec:java -pl client`