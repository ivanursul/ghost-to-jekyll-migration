---
layout: "post"
title:  "Sending emails with Java, Spring and Handlebars"
date: 2015-02-07 18:37:04
permalink: sending-mails-with-javaxmail-handlebars
---


Nowadays, it's very popular to have applications, that can somehow notificate people.We're gonna use emails to send text and html mails to someone.Whar requirements do we need?

Possibility to send simple text emails
Posibility to send html emails.
Of course, to send emails, we need to have some template engine. We will use handlebars for that.

We will use

Spring.
Maven as build tool
Java Mail package
Handlebars template engine
Greenmail for "mocking" email server.


Structure

![](assets/images/Screen-Shot-2015-01-19-at-8-06-50-PM.png)

We need to have four subpackages, exception package for having custom exceptions, model package for storing our model, sender package for main functionality and template package  for storing template service.

Your pom.xml should have similar structure

<script src="https://gist.github.com/johnyUA/264a2351757e145c3ab5.js"></script>

So, let's create

Exception package

<script src="https://gist.github.com/johnyUA/c97aff0de7c789b38d96.js"></script>

Model package 

<script src="https://gist.github.com/johnyUA/4261a67b1b07ef23fcbe.js"></script>

<script src="https://gist.github.com/johnyUA/2d80c5a00d3eec3d631b.js"></script>

<script src="https://gist.github.com/johnyUA/18826b54f157e3237cfb.js"></script>

<script src="https://gist.github.com/johnyUA/0fbeb7fa9ab14debfc68.js"></script>

We created all required models and resources for further developing, so let's start adding core functionality

<script src="https://gist.github.com/johnyUA/e906a849a0faa724f603.js"></script>

<script src="https://gist.github.com/johnyUA/5c54a21baa82610d2c14.js"></script>

Template package

Have ever you ever think about sending emails with html content ? Imagine, your application sends email with some pretty organized html page. Sound great.

To do such things, of course, you need to have template engine.

I prefer Handlebards template engine.

<script src="https://gist.github.com/johnyUA/9fa009b8a6a369d36bc3.js"></script>

<script src="https://gist.github.com/johnyUA/60fd72b167cb6243c74f.js"></script>[gist id="60fd72b167cb6243c74f"]

if you have template in your resources/templates folder with name template1.html, then you should call handlebars.compile("template1"); in your code.

Usage

It's up to you how you will use this functionality, you can think about that on your spare time, I propose to use some sort of little services for each case, for example: sometimes you definitely know, that you will have constant amount of recipients, that will receive email, sometimes you know what will be the topic of your email, so , for each of that cases you can write separate service.

Something like that.

<script src="https://gist.github.com/johnyUA/ed799886b391d3a2f5b0.js"></script>

<script src="https://gist.github.com/johnyUA/2c5e72334d709ffc7062.js"></script>

<script src="https://gist.github.com/johnyUA/92e4d8eebef16e6b46da.js"></script>

 

Resources

Your resources folder should like that

 

![](assets/images/Screen-Shot-2015-02-01-at-5-03-36-PM.png)

<script src="https://gist.github.com/johnyUA/10f0d4d029caf07f1fb0.js"></script>

Greenmail is a fake server, that will allow you to register your beans in Spring. Also, you can use greenforest in your tests, receive mails, etc.

<script src="https://gist.github.com/johnyUA/5b924f414b77801b51e1.js"></script>

<script src="https://gist.github.com/johnyUA/ad3eee93880d62899724.js"></script>

 

How to connect it with your spring application

To connect this example with your application, you need to include mail-context.xml in your Spring context.

I use something like this for importing my module.

<script src="https://gist.github.com/johnyUA/196e1baab252bf1c14d5.js"></script>

 

If you will have some questions - feel free to ask me.