---
layout: "post"
title:  "Dockerizing your apps"
date: 2016-05-26 14:16:54
permalink: dockerizing-your-app
tags: docker
---


###### Preface
As some of you may already know, from the last autumn-2015 I'm working as a software engineer in startup company called [Upwork](https://upwork.com), in one of teams. From the time I started working there, I realized, that working on freelance basis is completely different from office work, even if you're working on a good quality freelance job. Why ? At least, because you cant share some knowledge with your coworkers by cuf of coffee in your lunch time. So I decided to convert my blog to some R&D investigation. That's why I'd like to warn you, that everything here is just an investigation, which I do on my free time, so don't take it so critically, if you find something, which you don't agree with, just comment, and let's discuss. Technologies are changing every day, and we need to follow the trend, don't we ?

###### Why containers  ?
Before I can proceed with explanation in my head what is the definition of container, let's return to the near part times, where we did everything manually: the dyno age, where it was ok to assemble your proeject [war](https://en.wikipedia.org/wiki/WAR_(file_format)) manually, and deploy it to the server in the same way.
![alt](https://i.chzbgr.com/full/5328393728/h178595FF/)
Then, the new era started, and we got build systems. It was cool, because we now could build and deploy our software using build commands. But it wasn't the true way it shold be. Why ? Because it was clear for us, developers, how to build our software, but was it clear for [devops](https://en.wikipedia.org/wiki/DevOps) ? Of course, not, because there was lot's of build systems, and for each of them devops needed to find aproach. It was not ideal, because developers wrote dozens of deployment instructions. 

From the prospective of past times, now I understand, that if developer had to write some instructions for deployment or even scripts, then he could easily follow some standard or specification - a formal contract, which is known for developer and devops engineer: developer knows how to write deployment instuctions and devops know how to deploy this instuctions.

This resulted in so called application containers - a strucute, where your application is running, and it's easy to deploy it. One of this containers is [Docker](https://www.docker.com/). Why it's cool ? Because you can deploy application without knowing what's inside. Perfect.

###### What is docker ?
[Wiki](https://en.wikipedia.org/wiki/Docker_(software)) page describes docker as an "open-source project that automates the deployment of applications inside software containers". So, it's a thing called container. 
Docker containers wrap up a piece of software in a complete filesystem that contains everything it needs to run: code, runtime, system tools, system libraries – anything you can install on a server. This guarantees that it will always run the same, regardless of the environment it is running in. So, instead of creating some instructions - you create a container.

###### How it works ?
On a high level, you have a **Dockerfile** for your project, and you it describes how container should be started. Docker has a good documentation, and there's no sense on copying words from there. So, just refer to [this](https://docs.docker.com/engine/reference/builder/) part of documentation, and it should be clear for you.

How this Dockerfile looks like ?

<script src="https://gist.github.com/ivanursul/6f13f9e5f3c343d73db07c0903e0d684.js"></script>

###### How can you I it ?

    docker build -t learning/getting-started .

###### How can I start it ?

    docker run -ti --rm learning/getting-started bash

`-t` means docker will attach preudo-container, 
`-i` means it's running in interactive mode,
`--rm` means it'll be removed as soon as you quit interactive mode.


###### Docker Hub
It's a place, where all docker images are located. Think about hub as a version control system. The same here, you create a new version of your image, you push it to hub. Visit [hub.docker.com](https://hub.docker.com/) for more information. Try doing following command to understand how it works
       
    docker search ${some-image-name}
    docker search ubuntu // example



###### How can it help me in my daily job ?
Imagin you have a new person on your project. You project is a complex system, which deploys not in a trivial way, and you have to show your new developer how he should start your project. Of course, you can say, that you can write a good README file, and your devs will be happy to read it and start working. And there'll be no failures or errors. True story, what else to say.
![alt](http://i.makeagif.com/media/10-01-2015/hN7OrQ.gif)
Instead of introducing your project in such insufficient way, delegate this work docker. Let him know how to start everything.

###### We love os x. How to work with docker on it ?
The recommended way to work with Docker is to use [Docker Toolbox](https://www.docker.com/products/docker-toolbox). 

###### Okay, if I'll fully rely on docker, how can I use it in all my services, including load balancers, etc ?
You can use [docker-compose](https://docs.docker.com/compose/). The idea is that you write one file called docker-compose.yml and describe which services do you need. You can describe all the containers, that you need, including load balancers, service discoveries, etc...

Here's how docker-compose.yml should look like:
```
web:
  build: .
  ports:
   - "8000:8000"
  links:
   - redis
redis:
  image: redis
```
Take a look at following line

	build .

It means, that web is a custom container, and you should look for **Dockerfile** in the same folder, from which you start your compose.

Another interesting part of compose file is **links**. What are they ?

then you do

	docker-compose up
under root folder, which contains docker-compose.yml and it should
build your system.

PS - Following [article](https://examples.javacodegeeks.com/devops/docker/docker-compose-example/) works fine as an example.

![alt](/content/images/2016/05/Screen-Shot-2016-05-26-at-1-44-39-PM.png)

###### Docker Compose. What beast is it ?

###### Can I run docker on production servers ? How? Is it worth doing that ?

At the moment, I see two options:

* Deploy your Docker image to Docker Hub, Install Docker on your prod machine, pull your image from Docker Hub and start it
* User Docker Swarm


###### Links
* https://examples.javacodegeeks.com/devops/docker/introduction-docker-java-developers/
* https://examples.javacodegeeks.com/devops/docker/docker-compose-example/
* http://blog.hypriot.com/post/docker-compose-nodejs-haproxy/
* https://docs.docker.com/compose/compose-file/

