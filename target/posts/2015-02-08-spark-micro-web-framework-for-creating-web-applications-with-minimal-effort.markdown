---
layout: "post"
title:  "Spark - Micro Web Framework for creating web applications with minimal effort"
date: 2015-02-08 09:19:14
permalink: spark-micro-web-framework-for-creating-web-applications-with-minimal-effort
tags: java,programming
---


Today I am going to write few words about Spark framework - micro framework for rapid web application development.

Requirements

* Java 8 installed. We're goind to use lambdas.
* Maven 3.+
* Eclipse IDE
* Creating project

Navigate to your project desired location and execute in terminal:

> mvn archetype:generate -DgroupId=org.ivanursul.spark -DartifactId=spark-example -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false

Then open your IDE and import maven project

#### Adding dependency

To include required libraries just add one dependency

> &lt;dependency>
    &lt;groupId>com.sparkjava&lt;/groupId>
    &lt;artifactId>spark-core&lt;/artifactId>
    &lt;version>2.1&lt;/version>
&lt;/dependency>

Don't forget to add maven-compiler-plugin

<script src="https://gist.github.com/johnyUA/0874e5239adc82dee517.js"></script>

Now you are ready to write some code.

Official site says, that it is enough to write something like


	import static spark.Spark.*;
    public class HelloWorld {
        public static void main(String[] args) {
            get("/hello", (req, res) -> "Hello World");
        }
    }
    
and after execution of program you car test this app by typing

http://localhost:4567/hello
Seems to be very easy, but let's try something harder.

#### Implementing CRUD operations

To implement CRUD(create, remove, update, delete) for, say, person entity, we will gonna need:

DTO(Person)
Serializer/Deserializer(GSON? :) )
Note, that I will do it as simple, as I can, without any structures, just for testing this framework.

#### Let's start

Add gson dependency

> &lt;dependency>
 &lt;groupId>com.google.code.gson&lt;/groupId>
 &lt;artifactId>gson&lt;/artifactId>
 &lt;version>2.3.1&lt;/version>
&lt;/dependency>

**Create person class**

<script src="https://gist.github.com/johnyUA/daf4ad2580eed421fb7f.js"></script>

**Create main class**

<script src="https://gist.github.com/johnyUA/6d918d4c72188844e9aa.js"></script>

That seems to be all, that is needed.

What did we made ?

* Create method with POST http method
* Update method with PUT http method
* Get method with GET http method
* Delete method with DELETE http method
* We created a static Map to store persons
* We used gson to serialize/deserialize data

#### How to test?

If you have Postman extension for chrome, you can try to import my collection for this tutorial

https://www.getpostman.com/collections/071a319d71ab1a639c69

#### Impressions

Really, I see, that this framework is very easy to learn, easy to start, but not easy to maintain.

For quick POC's this framework is quite good, but for something really big and scalable - I think not.

The problem will occur, when I will try to change something, then I will notice, that there are static fields, classes, everywhere, and It will be very hard to maintain such code.

#### Resources

You can find github project [here](https://github.com/johnyUA/spark-getting-started).