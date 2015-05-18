# Docker authentication and authorisation on Kubernetes cluster

This is a description of the steps to deploy a complex microservice solution to Google Cloud Platform using
[kubernetes](http://kubernetes.io/) as a single Pod.

I'm writing this to gain experience on using a container service and try out the
scaling/deployment offered.

The solution is a web application using microservices to implement authentication and authorisation, consisting of 5
microservices and Nginx with Lua scripting, described [here](https://github.com/stevenalexander/docker-authentication-authorisation).

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

These container images are largely the same as the definitions from the [Development Docker Compose file](https://github.com/stevenalexander/docker-authentication-authorisation/blob/master/dev-docker-compose.yml) with the exception that the jars/config files are copied into the images to avoid needing mounted volumes.

```
# Build
docker build -t stevena/single-pod-nginx-lua:latest      -f kubernetes/single-pod-solution/Dockerfile-nginx-lua .
docker build -t stevena/single-pod-frontend:latest       -f kubernetes/single-pod-solution/Dockerfile-frontend .
docker build -t stevena/single-pod-authentication:latest -f kubernetes/single-pod-solution/Dockerfile-authentication .
docker build -t stevena/single-pod-authorisation:latest  -f kubernetes/single-pod-solution/Dockerfile-authorisation .
docker build -t stevena/single-pod-session:latest        -f kubernetes/single-pod-solution/Dockerfile-session .
docker build -t stevena/single-pod-person:latest         -f kubernetes/single-pod-solution/Dockerfile-person .

# Tag
docker tag stevena/single-pod-nginx-lua      gcr.io/PROJECTID/single-pod-nginx-lua
docker tag stevena/single-pod-frontend       gcr.io/PROJECTID/single-pod-frontend
docker tag stevena/single-pod-authentication gcr.io/PROJECTID/single-pod-authentication
docker tag stevena/single-pod-authorisation  gcr.io/PROJECTID/single-pod-authorisation
docker tag stevena/single-pod-session        gcr.io/PROJECTID/single-pod-session
docker tag stevena/single-pod-person         gcr.io/PROJECTID/single-pod-person

# Publish
gcloud preview docker push gcr.io/PROJECTID/single-pod-nginx-lua
gcloud preview docker push gcr.io/PROJECTID/single-pod-frontend
gcloud preview docker push gcr.io/PROJECTID/single-pod-authentication
gcloud preview docker push gcr.io/PROJECTID/single-pod-authorisation
gcloud preview docker push gcr.io/PROJECTID/single-pod-session
gcloud preview docker push gcr.io/PROJECTID/single-pod-person
```

4. Create the Cluster, Pod and allow external web access

```
# Create cluster
gcloud alpha container clusters create ms-auth --num-nodes 1 --machine-type g1-small
gcloud config set container/cluster ms-auth

# Pods
gcloud alpha container kubectl create -f kubernetes/single-pod-solution/single-pod.json
# see pod and get external IP from HOST column `gcloud alpha container kubectl get pods`

# Allow external web access
gcloud compute firewall-rules create k8s-ms-auth-node-80 --allow tcp:80 --target-tags k8s-ms-auth-node

# View site by pod external IP

# DEBUG
# check pod logs with `gcloud alpha container kubectl log single-ms-auth person`,
# ssh onto node with `gcloud compute ssh k8s-ms-auth-node-1`, access container `sudo docker exec -it CONTAINERID bash`
```

## Clean up

```
gcloud alpha container clusters delete ms-auth
gcloud compute firewall-rules delete k8s-ms-auth-node-80
```

## Conclusion

This architecture is a single [pod](https://github.com/GoogleCloudPlatform/kubernetes/blob/master/docs/pods.md) running
on a single node with all the components. It's basically the same as running it locally for development and doesn't
offer any horizontal scales. Didn't require much changes to the configuration, only updating server names to localhost
(containers in a pod can address each other locally). It could be scaled by creating a data persistence pod for a
database, updating config to point all the services which need persistence at it, then placing a load balancer in front
of the pod and creating multiple nodes hosting the web/data persistence pod.

Going forward I would like to work with volumes, split the services into individual pods/services to allow full scaling and try out separate data persistence.

I noticed a couple of things while trying to get this working:

- You can't open firewall access to the pod to non-standard ports (e.g. port 8081), something blocks access to that port outside the node. I ended up ssh'ing on the node and testing these locally.
- The nodes won't automatically check the latest version of the image if you update latest and push it to the repository. There is probably some command to force this or you need to name your versions explicitly.
