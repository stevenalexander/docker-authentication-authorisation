# Creating Kubernetes cluster Nginx with Lua on a single Pod

Steps to create a custom Nginx instance with Lua scripting running on a Kubernetes cluster with a single Pod.

Serves as a simple example of how to create and publish a custom docker image to

Requires:
* [Google Cloud](https://cloud.google.com/) account (free trial needs payment details)
* [gcloud](https://cloud.google.com/container-engine/docs/before-you-begin#install_the_gcloud_command_line_interface) tools (I recommend you do one of the [container engine tutorials](https://cloud.google.com/container-engine/docs/tutorials/hello-wordpress) first)
* [Docker](https://www.docker.com/)

## Building and publishing the custom Nginx image

To use a non-standard image in Google Cloud you will first need to build and publish it to [Google Container Registry](https://cloud.google.com/tools/container-registry/).

```
# Build
docker build -t stevena/nginx-lua-example:latest kubernetes/nginx-lua-example/.
# Test image locally by running 'docker run -i -p 80:80 stevena/nginx-lua-example'

# Tag image with Google Cloud project id
docker tag stevena/nginx-lua-example gcr.io/PROJECTID/nginx-lua-example

# Push the image to the registry
gcloud preview docker push gcr.io/PROJECTID/nginx-lua-example
```

## Setup

```
# Login
gcloud auth login

# Set project (created through dashboard)
gcloud config set project PROJECTID

# Set your zone
gcloud config set compute/zone europe-west1-b

# Create cluster with one node
gcloud alpha container clusters create nginx-lua --num-nodes 1 --machine-type g1-small

# Set to use the cluster
gcloud config set container/cluster nginx-lua

# Create the Pod
gcloud alpha container kubectl create -f kubernetes/nginx-lua-example/nginx-lua-example-pod.json

# allow external traffic
gcloud compute firewall-rules create nginx-lua-node-80 --allow tcp:80 --target-tags k8s-nginx-lua-node

# get IP from the HOST column for the Pod (not IP column)
gcloud alpha container kubectl get pod nginx-lua-example
```

You should now be able to curl/visit the external IP of your pod and see "Hello world by Lua!".

## Clean up

```
gcloud alpha container clusters delete nginx-lua

gcloud compute firewall-rules delete nginx-lua-node-80
```
