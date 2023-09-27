# Introduction

This is the basic implementation of the Life Insurance Multi Module Quotation Service. Each service was coverted to a Docker Image, with a Docker file contained within each service and a docker compose file in the root directory to coordinate the build process. 

Each service uses the broker service as a proxy to register itself in the registry. This is because each container are seperate and RMI can not bind to a registry in a different container so a proxy is used to register the object.

### Running the Program (From Root Directory)

1. To remove any target files from a pervious run: `mvn clean`
2. To package all maven modules, run: `mvn package`
3. Create the docker image: `docker compose build`
4. Run the containers: `docker compose up`