# Authentication and authorisation on Kubernetes cluster

This is a description of the steps to deploy the Docker Authentication and authorisation solution on a
[kubernetes](http://kubernetes.io/) cluster, hosted on Google Cloud platform, fully split into pods/services so it can
be scaled/load balanced.

The original source is [here](https://github.com/stevenalexander/docker-authentication-authorisation). I used MySql pods
for persisting data to make the session/person pods stateless, which is described [here](https://github.com/stevenalexander/docker-authentication-authorisation/blob/master/kubernetes-persistent-disks.md).

![Kubernetes architecture diagram](https://raw.githubusercontent.com/stevenalexander/docker-authentication-authorisation/master/images/microservice-authentication-and-authorisation-kubernetes-scaling-architecture.jpg "Kubernetes architecture diagram")

Based on the Kubernetes [Guestbook](https://cloud.google.com/container-engine/docs/tutorials/guestbook) tutorial.

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

2. Login to gcloud and set project/zone

```
gcloud auth login
gcloud config set project PROJECTID
gcloud config set compute/zone europe-west1-b
```

3. Build the container images and publish to [Google Container Registry](https://cloud.google.com/tools/container-registry/)

```
# Build
docker build -t stevena/replicated-nginx-lua:latest      -f kubernetes/replicated/dockerfiles/Dockerfile-nginx-lua .
docker build -t stevena/replicated-frontend:latest       -f kubernetes/replicated/dockerfiles/Dockerfile-frontend .
docker build -t stevena/replicated-authentication:latest -f kubernetes/replicated/dockerfiles/Dockerfile-authentication .
docker build -t stevena/replicated-authorisation:latest  -f kubernetes/replicated/dockerfiles/Dockerfile-authorisation .
docker build -t stevena/replicated-session:latest        -f kubernetes/replicated/dockerfiles/Dockerfile-session .
docker build -t stevena/replicated-person:latest         -f kubernetes/replicated/dockerfiles/Dockerfile-person .

# Tag
docker tag stevena/replicated-nginx-lua      gcr.io/PROJECTID/replicated-nginx-lua
docker tag stevena/replicated-frontend       gcr.io/PROJECTID/replicated-frontend
docker tag stevena/replicated-authentication gcr.io/PROJECTID/replicated-authentication
docker tag stevena/replicated-authorisation  gcr.io/PROJECTID/replicated-authorisation
docker tag stevena/replicated-session        gcr.io/PROJECTID/replicated-session
docker tag stevena/replicated-person         gcr.io/PROJECTID/replicated-person

# Publish
gcloud preview docker push gcr.io/PROJECTID/replicated-nginx-lua
gcloud preview docker push gcr.io/PROJECTID/replicated-frontend
gcloud preview docker push gcr.io/PROJECTID/replicated-authentication
gcloud preview docker push gcr.io/PROJECTID/replicated-authorisation
gcloud preview docker push gcr.io/PROJECTID/replicated-session
gcloud preview docker push gcr.io/PROJECTID/replicated-person
```

4. Create the Google Cloud persistent disk for the MySql databases

```
# size is 200GB for performance recommendations https://developers.google.com/compute/docs/disks/persistent-disks#pdperformance
gcloud compute disks create --size 200GB replicated-session-mysql-disk
gcloud compute disks create --size 200GB replicated-person-mysql-disk
```

5. Create the Cluster, Pod and allow external web access

```
# Create cluster
gcloud alpha container clusters create replicated-ms-auth --num-nodes 7 --machine-type n1-standard-1
# 8 instances is the default max quota, so 7 nodes plus 1 master
gcloud config set container/cluster replicated-ms-auth

# Create Mysql pods and services
gcloud alpha container kubectl create -f kubernetes/replicated/pods/person-mysql-pod.yaml
gcloud alpha container kubectl create -f kubernetes/replicated/pods/session-mysql-pod.yaml

gcloud alpha container kubectl create -f kubernetes/replicated/services/person-mysql-service.yaml
gcloud alpha container kubectl create -f kubernetes/replicated/services/session-mysql-service.yaml

# Create microservices pods and services
gcloud alpha container kubectl create -f kubernetes/replicated/pods/frontend-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/frontend-service.json

gcloud alpha container kubectl create -f kubernetes/replicated/pods/authentication-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/authentication-service.json

gcloud alpha container kubectl create -f kubernetes/replicated/pods/authorisation-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/authorisation-service.json

gcloud alpha container kubectl create -f kubernetes/replicated/pods/session-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/session-service.json

gcloud alpha container kubectl create -f kubernetes/replicated/pods/person-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/person-service.json

# Create Nginx pod and service with load balancer
gcloud alpha container kubectl create -f kubernetes/replicated/pods/nginx-lua-pod.json
gcloud alpha container kubectl create -f kubernetes/replicated/services/nginx-lua-service.json

# Allow external web access
gcloud compute firewall-rules create k8s-replicated-ms-auth-node-80 --allow tcp:80 --target-tags k8s-replicated-ms-auth-node

# DEBUG
# check pod logs with `gcloud alpha container kubectl log nginx-lua-2mspc`,
# ssh onto node with `gcloud compute ssh k8s-replicated-ms-auth-node-1`, access container `sudo docker exec -it CONTAINERID bash`
```

## Clean up

```
gcloud alpha container clusters delete replicated-ms-auth
gcloud compute firewall-rules delete k8s-replicated-ms-auth-node-80
gcloud compute disks delete replicated-session-mysql-disk
gcloud compute disks delete replicated-person-mysql-disk

# NOTE you must delete these as if you try to recreate your service with `createExternalLoadBalancer=true` it will fail
# silently if existing target-pools and forwarding-rules exist for your nodes
# Get IDs from `gcloud compute forwarding-rules list` and `gcloud compute target-pools list`
gcloud compute forwarding-rules delete RULEID
gcloud compute target-pools delete POOLID
```

## Conclusion

I'm very impressed with how easy it was make [Pods](https://github.com/GoogleCloudPlatform/kubernetes/blob/master/docs/pods.md)
and [services](https://github.com/GoogleCloudPlatform/kubernetes/blob/master/docs/services.md) for each of my
microservices. While there is an individual json/yaml definition file per pod and service, most are identical except for
names/labels and minor configuration. Kubernetes wires together services (which are available across the cluster) out of
the pods using labels, so any pod labelled "name=authentication" is selected as part of the Authentication service.
Traffic to services is distributed across the pods randomly by [kube-proxy](https://github.com/GoogleCloudPlatform/kubernetes/blob/master/docs/services.md#user-content-portals-and-service-proxies)
running on each node.

It took a little bit of time to understand that the pods are hosted randomly across the cluster nodes, their host
location isn't meant to be important as the pods can be created and destroyed as needed on the cluster. In this setup
you could kill individual pods and the replication controllers would just spin up replacements. Even killing individual
cluster nodes should not affect the system, as there are redundant pods spread across the other nodes.

While I've created this solution using Google Cloud platform and tools Kubernetes is platform independent, so you can
create your own cluster on any servers (see here for a [redhat tutorial](https://access.redhat.com/articles/1198103)).
This means you aren't locked into a provider and can deploy your solution using Kubernetes for
controlling your containers on multiple providers and still meet project specific hosting requirements on production (security etc.)

Future improvements and questions:

- Use environmental variables for configuration rather than hard coding in the docker files
- Find a better way to manage logging/monitoring (graphite container?)
- Find a better way to startup and restart pods
- Find a way to force nodes to retrieve the latest version of the image from a repository (currently its caching and not checking for updates)
- Fully script building the cluster
- How to do rolling updates
- How to manage database updates/migrations/backups
