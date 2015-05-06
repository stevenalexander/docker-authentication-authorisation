# Docker authentication and authorisation images

Example solution using Docker to create a number of containers which together implement a microservices based web
application with authentication and authorisation.

I created this project to test using Docker as part of the development process to reduce the separation between
developers and operations. The idea being that developers create and maintain both the code and the containers that
their code will run in, including scripts/tools used to configure and setup those containers. Hopefully this will reduce
the knowledge gap that forms a barrier between developers and operations in projects, causing problems when developers
push code that breaks in production ("throwing over the wall" at operations).

I'm aware that Docker and containers in general are not a cure-all for 'devOps', they are only an abstraction that
tries to make your applications run in an environment as similar to production as possible and make deployment/setup
more consistent.

Containers running locally or on a test environment are not the same as the solution running on production. There are
concerns about performance/networking/configuration/security which developers need to understand in order to produce
truly production ready code that de-risks regular releases. Creating a 'devOps' culture to decrease the time necessary
to release and increase quantity requires a change in process and thinking, not just technology.

## Details

The solution is composed of microservices, using [nginx](http://nginx.org/) as a reverse proxy and
[Lua](http://wiki.nginx.org/HttpLuaModule) scripts to control authentication/sessions. Uses [Docker](https://www.docker.com/)
and [Docker Compose](https://docs.docker.com/compose/) to build container images which are deployed onto a Docker host
VM.

### Microservices

- [Authentication](tree/master/microservices/authentication) - used to authenticate users against a set of stored
credentials
- [Session](tree/master/microservices/session) - used to create and validate sessions for authenticated users
- [Authorisation](tree/master/microservices/authorisation) - used to check authenticated users permissions to perform
actions
- [Person](tree/master/microservices/person) - used to retrieve and update person details, with the actions a user can
perform controlled by the Authorisation application
- [Frontend](tree/master/microservices/frontend) - HTML UI wrapper for the login/person functionality

### Development architecture

TODO image

This is a small scale architecture intended for local development. Developers can spin this up quickly and work on the
full application stack.

Uses [Docker Compose](https://docs.docker.com/compose/) to start the linked containers with appropriate configuration.

### Small realistic architecture

TODO image

This is a larger scale architecture, using HAProxy to load balance and introduce redundancy.

Still uses [Docker Compose](https://docs.docker.com/compose/) and runs on a single Docker Host. It gives developers an
idea of what they need to do to make their components run on an environment with multiple instances.

### Production architecture

TODO image

This is an example production architecture, running on multiple Docker hosts with redundancy.

On a real production architecture you would want to include:
- Healthchecks
- Monitoring
- Periodic backups

TODO scripts for spinning up production environment on EC2 etc.
TODO tests and chaos monkey style kill tests

## Running the containers

Requires:
* [Docker](https://www.docker.com/)
* [Boot2Docker](http://boot2docker.io/)
* [Docker-compose](http://docs.docker.com/compose/)
* JDK (to compile java file locally)
* [Gradle](https://gradle.org/) (for build automation)

```
# Build
gradle buildJar

# Run containers with dev architecture
docker-compose -f dev-docker-compose.yml up

# curl your boot2docker VM IP on port 8080 to get the login page
```

## TODO list

- smart redirects on 401 when no AccessToken, saving target URL in query and redirecting when logged in
	- on authentication fail should display validation error
- main template should look for callerId header and show login/logout accordingly
- 401/404/500 pages
- tests
- architecture diagrams/sequence diagram
