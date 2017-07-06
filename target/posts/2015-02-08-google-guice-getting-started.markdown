---
layout: "post"
title:  "Google Guice - Hello world example"
date: 2015-02-08 21:09:36
permalink: google-guice-getting-started
tags: java,programming
---


It has been more than two years since I first acquainted with Spring Dependency Injection technology.

At first, I didn't understood what it was, but after few weeks I realized what powerful instrument do I have.

From that time I started to love this type of object instantiation and I really don't want to instantiate objects in some other way.

But today I want to represent you a new framework for bean instantiation - Google Guice.

So, how to try this Google Guice dependency injection framework ?

First of all, open your Eclipse/InteliJIDEA IDE and create maven project: write groupId, artifactId and other things.

After that, go to your pom.xml file and add additional dependency:

<script src="https://gist.github.com/johnyUA/60718dbd62173fee3315.js"></script>

Then, you should create interface NotificationService:

<script src="https://gist.github.com/johnyUA/3084d08aa96ccbf7c904.js"></script>

We will have multiple implementations of above service to show posibillities of Google Guice

<script src="https://gist.github.com/johnyUA/448f6f104c490b26ce89.js"></script>

Email service is one of the implementations of NotificationService

One thing that I noticed is that Google Guice has it's own @Singleton annotation - from JSR330.

Google Guice 3.0 added the support for JSR-330 and we can use annotations from com.google.inject or javax.inject package.

Next, we will create second implementation of NotificationService - FacebookService:

<script src="https://gist.github.com/johnyUA/a4a8ad0c60a2ecc8fe2a.js"></script>

Consumer class

Since we are implementing dependency injection in our application, we won’t initialize the service class in application. Google Guice support both setter-based and constructor-based dependency injection. Our application class that consumes the service looks like below.

<script src="https://gist.github.com/johnyUA/71429d73d4d58088ae45.js"></script>

Or, if you want constructor-based instantiation

<script src="https://gist.github.com/johnyUA/b0cb409aa635db568ee3.js"></script>

Binding Service implementation

Obviously google guice will not know which service to use, we have to configure it by extending AbstractModule abstract class and provide implementation for configure() method.

<script src="https://gist.github.com/johnyUA/33b0de2619d8180b9264.js"></script>

As you can see that we can bind any of the implementation to service class. For example, if we want to change to EmailService we would just need to change the bindings.

Client Application

Our setup is ready, let’s see how to use it with a simple java class.

<script src="https://gist.github.com/johnyUA/7b78e07f2b584cc0a585.js"></script>

The implementation is very easy to understand. We need to create Injector object using Guice class createInjector() method where we pass our injector class implementation object. Then we use injector to initialize our consumer class. If we run above class, it will produce following output.

Message sent to Facebook user Ivan@mail.com with message=Hi, Ivan

Use of Google Guice for implementing dependency injection in application is very easy and it does it beautifully. It’s used in Google APIs so we can assume that it’s highly tested and reliable code. Download the project from above and play around with it to learn more.