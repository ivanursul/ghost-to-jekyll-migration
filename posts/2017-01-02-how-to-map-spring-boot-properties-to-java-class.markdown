---
layout: "post"
title:  "How to map Spring Boot properties to Java class"
date: 2017-01-02 21:08:01
permalink: how-to-map-spring-boot-properties-to-java-class
---


Have you ever had a need to use some values from **application.properties** or **application.yml? How did you take them out?

Personally, I always used [@Value](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/annotation/Value.html) annotation:

```
    @Value("${graphite.host}")
    private String graphiteHost;
```

It wasn't the best way to work with my properties. However, I didn't know a better approach.

Then, I found [@ConfigurationProperties](http://docs.spring.io/spring-boot/docs/1.1.7.RELEASE/api/org/springframework/boot/context/properties/ConfigurationProperties.html) - annotation from Boot package, which has everything you need to map your properties. 

### <a href="#given" name="given"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Given

Let's say, your **application.yml** looks like following:

```
graphite:
  enabled: true
  host: localhost
  port: 2003
  amountOfTimeBetweenPolls: 20000
```

### <a href="#when" name="when"><i class="fa fa-link anchor" aria-hidden="true"></i></a> When


You need to create a bunch of classes, which you will be autowiring in all parts of your code.

I'm using [Project Lombok](https://projectlombok.org/) for skipping java formalities, if you are not, then create getters and setters for your classes. 

```
package org.rngr.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@ConfigurationProperties(ignoreUnknownFields = true)
@Data
public class ApplicationProperties {

    @NotNull
    private GraphiteProperties graphite;

}
```

Pay attention to `@ConfigurationProperties` annotation, it's playing a key role here.

Don't forget about subclass:

```
package org.rngr.properties;

import lombok.Data;

@Data
public class GraphiteProperties {

    private boolean enabled;
    private String host;
    private int port;
    private int amountOfTimeBetweenPolls;

}
```

In the end, you need to enable configuration properties:

```
package org.rngr;

import lombok.extern.slf4j.Slf4j;
import org.rngr.config.*;
import org.rngr.properties.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		MonitoringConfiguration.class
})
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class WebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}
}
```

### <a href="#then" name="then"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Then

Then, you are free to Autowire **ApplicationProperties** instance wherever you want:

```
    ...
    @Autowired
    private ApplicationProperties properties;
    ...
```

### <a href="#conclusions" name="conclusions"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Conclusions

I guess, this approach was [borrowed](https://github.com/dropwizard/dropwizard/blob/master/dropwizard-core/src/main/java/io/dropwizard/Configuration.java) from Dropwizard framework. One good thing about introducing configuration properties class is that you will get power to validate them.

I remember a few years ago there was a broad discussion about pros and cons of switching to Java configuration. For me, Configuration properties and their transformation to ConfigurationProperties class is something similar.

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Empowering your apps with Spring Boot's property support](https://spring.io/blog/2013/10/30/empowering-your-apps-with-spring-boot-s-property-support)