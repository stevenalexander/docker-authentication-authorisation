# Docker authentication and authorisation on Kubernetes cluster

This is a description of the steps to deploy a microservice on a [kubernetes](http://kubernetes.io/) cluster with
persistent storage via a database pod.

I'm using an existing microservice I created for storing session details, described [here](https://github.com/stevenalexander/docker-authentication-authorisation).

Based on the Kubernetes [Using Persistent Disks](https://cloud.google.com/container-engine/docs/tutorials/persistent-disk) tutorial.

Requires:
* [Google Cloud](https://cloud.google.com/) account (free trial needs payment details)
* [gcloud](https://cloud.google.com/container-engine/docs/before-you-begin#install_the_gcloud_command_line_interface) tools (I recommend you do one of the [container engine tutorials](https://cloud.google.com/container-engine/docs/tutorials/hello-wordpress) first)
* [Docker](https://www.docker.com/)
* JDK (to compile java file locally)
* [Gradle](https://gradle.org/) (for build automation)

## Setup

1. Build microservices and copy jar/config into volumes

```
gradle buildJar

```

You can test DB access locally by spinning up a [MySql](https://registry.hub.docker.com/_/mysql/) container, setting
the database environment variable to create the `session` DB, then starting the microservice container linked to the
MySql container. This mirrors how it will connect to the Kubernetes data Pod when it is run as a Service (connecting by
[Service host name](https://cloud.google.com/container-engine/docs/tutorials/persistent-disk/#mysql_service)).

```
# MySql container with environment variable to create DB and link to session microservice container
docker run --name session-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=session -d mysql:latest
docker run -it --link session-mysql:session-mysql --rm -p 8084:8084 stevena/persistent-disks-session:latest
```

2. Login to gcloud and set project/zone

```
gcloud auth login
gcloud config set project PROJECTID
gcloud config set compute/zone europe-west1-b
```

3. Build the container images and publish to [Google Container Registry](https://cloud.google.com/tools/container-registry/)

These container images are largely the same as the definitions from the [Development Docker Compose file](https://github.com/stevenalexander/docker-authentication-authorisation/blob/master/dev-docker-compose.yml) with the exception that the jars/config files are copied into the images to avoid needing mounted volumes.

```
# Build
docker build -t stevena/persistent-disks-session:latest -f kubernetes/persistent-disks/Dockerfile-session .

# Tag
docker tag stevena/persistent-disks-session gcr.io/PROJECTID/persistent-disks-session

# Publish
gcloud preview docker push gcr.io/PROJECTID/persistent-disks-session
```

4. Create the Google Cloud persistent disk for MySql

```
# size is 200GB for performance recommendations https://developers.google.com/compute/docs/disks/persistent-disks#pdperformance
gcloud compute disks create --size 200GB persistent-disks-session-mysql-disk
```

5. Create the Cluster, Pod and allow external web access

```
# Create cluster
gcloud alpha container clusters create session-persist --num-nodes 2 --machine-type g1-small
gcloud config set container/cluster session-persist

# MySql pod and Service
gcloud alpha container kubectl create -f kubernetes/persistent-disks/mysql-pod.yaml
gcloud alpha container kubectl create -f kubernetes/persistent-disks/mysql-service.yaml

# Session pod
gcloud alpha container kubectl create -f kubernetes/persistent-disks/session-pod.yaml
# see pod and get external IP from HOST column `gcloud alpha container kubectl get pods`

# Allow external web access
gcloud compute firewall-rules create k8s-session-persist-node-80 --allow tcp:80 --target-tags k8s-session-persist-node

# cURL service
curl 'http://PODIP/api/sessions' --data-binary '1234'
curl 'http://PODIP/api/sessions/ACCESSTOKENFROMABOVE'

# DEBUG
# check pod logs with `gcloud alpha container kubectl log single-session-persist session`,
# ssh onto node with `gcloud compute ssh k8s-session-persist-node-1`, access container `sudo docker exec -it CONTAINERID bash`

# Delete MySql pod and restart to test persisted data
gcloud alpha container kubectl delete -f kubernetes/persistent-disks/mysql-pod.yaml
# curl service and see error as DB is down
gcloud alpha container kubectl create -f kubernetes/persistent-disks/mysql-pod.yaml
# curl service with previously created token and see it has restarted with data from persistent disk
```

## Clean up

```
gcloud alpha container clusters delete session-persist
gcloud compute firewall-rules delete k8s-session-persist-node-80
gcloud compute disks delete persistent-disks-session-mysql-disk
```

## Conclusion

I like the idea of easily creating database containers per microservice, saving all persisted data on a store that can
be easily backed up and controlled. Using this approach avoids the performance trap of a monolithic database saving all
your data from the entire solution. The MySql pod started up extremely fast and there were no problems when I deleted
then restarted the pod.

Still a lot of things would need to be considered, replication, user control (I used root access for simplicity) and
data migration/updates. Most likely you would need to make custom Dockerfile database containers per store, which
includes logic for how to setup/control the container. Think I would need to sit down with a DBA to discuss how you
could manange and maintain production databases when using this approach.
