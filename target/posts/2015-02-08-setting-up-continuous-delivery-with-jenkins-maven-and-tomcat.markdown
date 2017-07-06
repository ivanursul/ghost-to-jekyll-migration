---
layout: "post"
title:  "Setting up continuous delivery with Jenkins, Maven and Tomcat"
date: 2015-02-08 00:09:26
permalink: setting-up-continuous-delivery-with-jenkins-maven-and-tomcat
---


Sometimes it's hard for me to deploy your application each time when you chang something in your code. Truly, if it is important for you to deploy your application at least twice per day, and the process of deployment will take up to 10 minutes, then it will result to +- 200 minutes per month and 2400 minutes per year!!! That's almost 2 days.

So, instead of doing deployment each time manually, I propose you to do that with your Continuous Integration tool.

The process of continuous deploy is called continuous delivery, which includes Build -> Deploy -> Test -> Release.

## Required tools

In this article you will need a couple of tools to succed

Continuous Integration tool. It is important to trigger every change you made in your code. For that needs you can use Jenkins continuous integration server.
Build tool. It is a common knowledge that nowadays we don't need to build our apps manually, now we build them by ready tools, that simplifies our development process. I will be using maven
for that.It helps me to manage my dependencies, split my application into several modules, has many plugins, can check the quality of my code.
Application server. If CI server will trigger change to your code - he will need to build and deploy your application somewhere. As application server, I use Tomcat.
## What's not covered

There will be no information on how to deal with maven, tomcat, and jenkins tools. You should be familiar with that tools.
Server configuration. You can read this article for better understanding. The only thing I would like to mention is that you need to have ssh access to your server.
Data Storage configuration. There will be issues with database, and you really need to setup you storage by your own.
Third party tools. I use migrations for database changes and there was some problems with this type of tools.
Preamble

## What you need to know about continuous delivery ?

Firstly, I would suggest to read wiki page.

In few words, it is a practice of automated delivery process. You don't need to deploy, build your application every time, because CI server will do that for you.

So, this mean that if we have web application, then every time when we will commit changes to git server, jenkins will trigger that, will build project, will create war archieve and deploy it to our dev server.

## Syllabus

For better understanding we will split our work into several parts:

Development server configuration.
Tomcat Maven plugin, configuration.
Jenkins configuration.
Setting up integration test job.
Development server configuration

On your dev server you will need to have next apps installed

Tomcat, running on 8080 port
Database, in my case, running on 5432 port
Java installed - 1.7 version
As I have mentioned before, you can use this article to setup your basic dev server.

After you setup all required software, you can to make some things:

Make ssh access to dev server from jenkins build tool. I will explain why: If you will need to do some work with database(running migrations, for example), you will need to access your database. For example, if you have postgres database, then you definetely know, that, by default, postgres is running on localhost on 5432 port and don't have access outside server, so, you can't connect to database by remote IP on port 5432. There is two options that you can do: configure postgresql to have access outside, which is undesirable or you can make ssh port forwarding , you will setup local port, for example, 2222 to forward everything on 5432 port on your remote dev server.
Tomcat enhancement. You can configure nginx to listen to 80 port and to redirect all requests to 8080 tomcat port, which will be closed to outside requests.
Start tomcat. You can make aliases to easily deploy Tomcat:

> alias tomcat-start='~/tools/tomcat/bin/startup.sh'

> alias tomcat-stop='~/tools/tomcat/bin/shutdown.sh'

Then execute in terminal

> tomcat-start

## Tomcat maven configuration

This part is required for our configuration because we will use tomcat maven plugin to deploy our application into dev-server.Note, that application will be deployed only in case of successful build.

## To make things work we will need:

Configuring new user in tomcat.
Add server into ~/.m2/settings.xml file on your jenkins machine. This part is needed because Tomcat requires to have such information about server.
Adding plugin.
Adding server

This is need to be done to be able to deploy your war into your tomcat.

Login into your dev-server, cd into your tomcat directory and execute:

nano ./conf/tomcat-users.xml
You should have something like this

<script src="https://gist.github.com/johnyUA/dc18ff0a37a5451fa94f.js"></script>

Don't forget to restart tomcat

Configuring maven settings.xml

This action should be done in jenkins server.

If maven will deploy our app into tomcat server, then maven should have tomcat credentials. Credentials are stored in your home directory/.m2/settings.xml

If your don't have such file, then create it or execute

> nano ~/.m2/settings.xml

and add next lines

<script src="https://gist.github.com/johnyUA/7bc0f10e14653d8a7640.js"></script>

id is server name, which will be specified in tomcat plugin.

## Adding plugin

To add tomcat maven plugin , add this in plugins section

<script src="https://gist.github.com/johnyUA/99c306ee8e7793536731.js"></script>

and properties section

<script src="https://gist.github.com/johnyUA/1d7fa79fd1b84f91973d.js"></script>

If you did everything right, then you should be able to deploy your application into your development server

> mvn tomcat7:redeploy

You should receive build success message.

## Jenkins configuration

Before this part I want to say, that I assume that you've already setup your main job, and it is working.

Now, if you did all previous steps, you can start configuring jenkins to make a deployment.

I'm assuming that you have already installed jenkins, added your job without deployment and you are waiting for build setup.In my application I have several modules, where one of them - is module with war packaging, so he is a submodule.

![](assets/images/Screen-Shot-2015-01-11-at-4-58-20-PM.png)

**is-lnu-rest-api**  is my **webapp**, so it will be the module, where we  will store our tomcat plugin.

Enter your jenkins server, open your job and press configure
![](assets/images/Screen-Shot-2015-01-11-at-5-02-12-PM.png)

jenkins job

Then go to Build section and press Add post-build step -> Invoke top level maven targets

![](assets/images/Screen-Shot-2015-01-11-at-5-06-54-PM-1024x417.png)

Adding top level maven targets
 

And add appropriate settings

![](assets/images/Screen-Shot-2015-01-11-at-5-20-00-PM-1024x517.png)

Now you can start building your application, and if you did everything right - After each commit your server will be redeployed.

## Setting up integration test job

So, what we have? We manage to configure our jenkins job to trigger git changes, run unit tests, make quality checks, and deploy application to some dev-server. We have another thing to do: integration testing. I want to run integration tests each time, when some changes happen. Do that make sense ? My position is that we definetely need to run those integration tests each time when changes happen. That will allow us to detect broken functionality as soon as possible.Of course, you need to have separate qa server for running those tests, but it is worth it.

So, what is the right way for running integration tests with continuous integration tools ? I didn't find exact answer for that, but in my opinion we need to trigger our main build, which will deploy application, and in case of success we should run integration tests.

Go to Jenkins main page, create job, simillar to your first job, but with other Build triggers.

![](assets/images/Screen-Shot-2015-01-12-at-9-12-18-PM-1024x260.png)

Choose your main project job.

Note, that you main job should handle project deploy, so, when you will start new job, that will run integration tests, you will be sure, that webapp is deployed and is up to date.

I'm not sure in which way you run your integration tests, but I have separate module for them, so to run them, I have next Build section

![](assets/images/Screen-Shot-2015-01-12-at-9-28-58-PM-1024x170.png)

 

However, there is a chance, that you have separate servers for dev and for qa. For such approach you need to modify second integration job to run tomcat:redeploy on qa server.

After all manipulations, you should have the following functionality:

Your main job will run junit tests, findbugs analys, checkstyle, etc...
Your main job will deploy war to remote dev server
Your integration job will run integration tests