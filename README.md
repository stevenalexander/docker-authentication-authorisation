# Docker authentication and authorisation images

This is sample implementation of the microservice authentication and authorisation pattern I described in a previous
blog posts ([here](https://stevenwilliamalexander.wordpress.com/2014/04/24/microservice-authentication-and-authorisation/)
for pattern, [here](https://stevenwilliamalexander.wordpress.com/2015/03/12/microservice-authentication-and-authentication-scaling/)
for how it could scale). It uses [Nginx](http://nginx.org/) with [Lua](http://wiki.nginx.org/HttpLuaModule) and
[Dropwizard](http://www.dropwizard.io/) for the microservices, provisioned into containers using [Docker](https://www.docker.com/).

Requires:
* [Docker](https://www.docker.com/)
* [Boot2Docker](http://boot2docker.io/)
* [Docker-compose](http://docs.docker.com/compose/)
* JDK (to compile java file locally)
* [Gradle](https://gradle.org/) (for build automation)

I created this project to test using Docker as part of the development process to reduce the separation between
developers and operations. The idea being that developers create and maintain both the code and the containers that
their code will run in, including scripts/tools used to configure and setup those containers. Hopefully this will reduce
the knowledge gap that forms a barrier between developers and operations in projects, causing problems when developers
push code that breaks in production ("throwing over the wall" at operations).

I'm aware that Docker and containers in general are not a cure-all for 'devOps', they are only an abstraction that
tries to make your applications run in an environment as similar to production as possible and make deployment/setup
more consistent. Containers running locally or on a test environment are not the same as the solution running on production. There are
concerns about performance/networking/configuration/security which developers need to understand in order to produce
truly production ready code that de-risks regular releases. Creating a 'devOps' culture to decrease the time necessary
to release and increase quantity requires a change in process and thinking, not just technology.

## Running the containers

```
# Build microservices and copy their files into volume directories
gradle buildJar

# Run containers with dev architecture
docker-compose -f dev-docker-compose.yml up

# curl your boot2docker VM IP on port 8080 to get the login page, logs are stored in docker/volume-logs
```

## Details

The solution is composed of microservices, using [nginx](http://nginx.org/) as a reverse proxy and
[Lua](http://wiki.nginx.org/HttpLuaModule) scripts to control authentication/sessions. Uses [Docker](https://www.docker.com/)
and [Docker Compose](https://docs.docker.com/compose/) to build container images which are deployed onto a Docker host
VM.

### Microservices

The solution is split into small web services focused on a specific functional area so they can be developed and
maintained individually. Each one has it's own data store and can be deployed or updated without affecting the others.

- [Authentication](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/authentication) - used to authenticate users against a set of stored
credentials
- [Authorisation](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/authorisation) - used to check authenticated users permissions to perform
actions
- [Frontend](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/frontend) - HTML UI wrapper for the login/person functionality
- [Person](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/person) - used to retrieve and update person details, intended as a simple example
of an entity focused microservice which links into the Authorisation microservice for permissions
- [Session](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/session) - used to create and validate accessTokens for authenticated users

There is an [Api](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/microservices/api) library containing objects used by multiple services (for real solution
should be broken up into API specific versioned libraries for use in various clients, e.g. personApi, authorisationApi).

### Nginx reverse proxy

Nginx is used as the reverse proxy to access the Frontend microservice and it also wires together the authentication and
session management using Lua scripts. To provision the Nginx container I created a DockerFile which installs nginx with
[OpenResty](http://openresty.org/)

- [Dockerfile](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/docker/image-nginx-lua/Dockerfile) - defines the Nginx container image, with modules for Lua
scripting
- [nginx.conf](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/docker/volume-nginx-conf.d/nginx.conf) - main config for Nginx, defines the endpoints
available and calls the Lua scripts for access and authentication
- [access.lua](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/docker/volume-nginx-conf.d/access.lua) - run anytime a request is received, defines a list of
endpoints which do not require authentication and for other endpoints it checks for accessToken cookie in the request
header then validates it against the Session microservice
- [authenticate.lua](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/docker/volume-nginx-conf.d/authenticate.lua) - run when a user posts to /login, calls
the Authentication microservice to check the credentials, then calls the Session microservice to create an accessToken
for the new authenticated session and finally returns a 302 response with the accessToken in a cookie for future
authenticated requests.
- [logout.lua](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/docker/volume-nginx-conf.d/logout.lua) - run when a user calls /logout, calls the Session
microservice to delete the users accessToken

#### Authentication and authorisation sequence diagram

![Authentication and authorisation sequence diagram](https://raw.githubusercontent.com/stevenalexander/docker-authentication-authorisation/master/images/microservice-authentication-and-authorisation-sequence.png "sequence diagram")

### Docker containers and volumes

The interesting thing about using Docker with microservices is that you can define a container image per microservice
then host those containers in various arrangements of Docker host machines to make your architecture. The containers can
be created/destroyed easily, give guarantees of isolation from other containers and only expose what you define
(ports/folders etc.). This makes them easily portable between hosts compared to something like a puppet module that
needs more care and configuration to ensure it can operate on the puppet host.

To develop and test the solution locally I used a development architecture defined in a
[Docker Compose](http://docs.docker.com/compose/) yaml file ([here](https://github.com/stevenalexander/docker-authentication-authorisation/tree/master/dev-docker-compose.yml)). This created a
number of containers with volumes and exposed ports then links them together appropriately.

Below shows architectures which can be built using the containers.

#### Development architecture

![Development architecture diagram](https://raw.githubusercontent.com/stevenalexander/docker-authentication-authorisation/master/images/microservice-authentication-and-authorisation-simple-architecture.jpg "Development architecture diagram")

This is a small scale architecture intended for local development. Developers can spin this up quickly and work on the
full application stack. It uses a single Docker host (the boot2docker VM) with single containers for each microservice.
This means that if any of the containers or services fail there is no redundancy.

#### Small scaled architecture

![Small scaled architecture diagram](https://raw.githubusercontent.com/stevenalexander/docker-authentication-authorisation/master/images/microservice-authentication-and-authorisation-small-scaled-architecture.jpg "Small scaled architecture diagram")

This is a larger scale architecture, using HAProxy to load balance and introduce redundancy. This architecture allows
scaling the business microservices to handle increasing/decreasing load.

#### Large scaling architecture

![Large scaling architecture diagram](https://raw.githubusercontent.com/stevenalexander/docker-authentication-authorisation/master/images/microservice-authentication-and-authorisation-large-scaling-architecture.jpg "Large scaling architecture diagram")

This is an example production architecture, running on multiple Docker hosts with redundancy for all microservices and
load balancing for the web servers. The number of hosts you have per container can be increased/decreased dynamically
based on the individual load on each service and each container can be updated without downtime by rolling updates.

On a real production architecture you would want to include:

- Healthchecks
- Monitoring (e.g. Dropwizard Metrics pushing to Graphite)
- Dynamic scaling based on load monitoring
- Periodic backups of peristed data
- Security testing

## Conclusions

I found working with Docker extremely easy, the tooling and available images made it simple to create containers to do
what I needed. For development the speed I could create and start containers for the microservices was amazing, 5 secs
to spin up the entire 6 container solution with Docker Compose. Compared to development using individual VMs provisioned
by Puppet and Vagrant this was lightning fast. Accessing the data/logs on the containers was simple also, making debug a
lot easier, and remote debug by opening ports was also possible.

Still have some concerns about how production ready my containers would be and what I would need to do to make them
secure. I did not touch on a lot of the work which would be necessary to create and provision the Docker hosts
themselves, including configuration of the microservices and Nginx containers per host. For a reasonable sized
architecture this would require a tool like Puppet anyway so would not save much effort on the operations side.

I would like a chance to use some sort of containerisation in a real project and see how it works out, in the
development side, operations for deployment in environment and in actual production use. For now I'd definitely
recommend developers to try it out for defining and running their local development environments as an alternative to
complex boxen/vagrant setups.

## Additions

### Google Cloud with Kubernetes

- [Publishing a custom docker image to Google private repository and running in a cluster as a single pod](https://github.com/stevenalexander/docker-authentication-authorisation/blob/master/kubernetes-nginx-lua.md)
- [Running the solution as a single pod](https://github.com/stevenalexander/docker-authentication-authorisation/blob/master/kubernetes-single-pod.md)
