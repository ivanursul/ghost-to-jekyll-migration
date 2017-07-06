---
layout: "post"
title:  "JHipster context is not loading on DigitalOcean cloud server"
date: 2015-04-30 20:17:15
permalink: jhipster-context-is-not-loading-on-digitalocean-cloud-server
---


Few weeks ago I started to work with [**JHipster**](https://jhipster.github.io/) - Java Framework for rapid development.I am not going to write a review of this framework - I hope, I will find time to do it later, but today I am going to write some useful information about pitfalls, that you can reach during your work with **JHipster**.

#####Jhipster project unable to start itself on digitalocean

Just FYI, Jhipster is built on top of Spring Boot Framework + Angular JS, so to start application, you need just to execute single command

	mvn spring-boot:run
    
That's a convenient way for deploying application - no separate application servers, only embedded, only hardcore.

That's why Spring Boot stores some embedded tomcat server, which contains all properties.

So, the problem is that sometimes, when you deploy your application on Digital Ocean cloud server - you need to wait for a long time to finish deploying process.

In my case, the problem with long deploy is that Tomcat loves to use **/dev/random** function, instead of **/dev/urandom**.

To get a good example of how random and urandom works, just go to terminal and type

	cat /dev/random

Then try to move your mouse, you will receive additional values.

Then type

	cat /dev/urandom

And you will get your result much more faster.

In Jhipster/Spring Boot project you can solve this issue with adding custom parameter

	-Djava.security.egd=file:/dev/./urandom

In spring-boot-maven-plugin   



        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <jvmArguments>-Djava.rmi.server.hostname=localhost -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -Djava.security.egd=file:/dev/./urandom</jvmArguments>
                <arguments>
                    <argument>--spring.profiles.active=dev</argument>
                </arguments>
            </configuration>
        </plugin>

I described this issue on official Jhipster page:
https://github.com/jhipster/generator-jhipster/issues/1425#issuecomment-97861947