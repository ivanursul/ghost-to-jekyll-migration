---
layout: "post"
title:  "Monitoring  your Spring application using Dropwizard metrics module"
date: 2016-08-13 21:29:28
permalink: monitoring-your-spring-application-using-dropwizard-metrics-module
tags: spring-framework,spring boot,dropwizard,metrics
---


Yes, it's true, that Spring is better, than Dropwizard. I've worked with both frameworks, and can truly say, that Dropwizard has poor Guice dependency injection, Jersey, which I don't like at all, and other things. But there's one thing, which I like in Dropwizard, and which I'd be happy to see in Spring Framework - [Dropwizard Metrics](http://metrics.dropwizard.io/3.1.0/) module. it has very rich number of instruments, which can help you to understand your application behaviour: different gauges, timers, counters, histograms, timers, and healthcheks.

### <a href="#thequestion" name="thequestion"><i class="anchor fa fa-link" aria-hidden="true"></i></a> The question is how to import Dropwizard Metrics into Spring? 

Basically, you need to add 3 dependencies:one as an adapter for dropwizard metrics, another is for jvm metrics and last one for Filter, which you will use to get this metrics using http.

> [com.ryantenney.metrics:metrics-spring:3.1.2](https://www.google.com.ua/search?q=com.ryantenney.metrics%3Ametrics-spring%3A3.1.2&oq=com.ryantenney.metrics%3Ametrics-spring%3A3.1.2&aqs=chrome..69i57.248j0j7&sourceid=chrome&ie=UTF-8)

> [io.dropwizard.metrics:metrics-jvm:3.1.2](https://mvnrepository.com/artifact/io.dropwizard.metrics/metrics-jvm/3.1.2)

> [io.dropwizard.metrics:metrics-servlets:3.1.2](https://mvnrepository.com/artifact/io.dropwizard.metrics/metrics-servlets/3.1.0)

Depending on what build tool you have in your application - include those libraries. I use Gradle, so my **build.gradle** looks like following: 

<script src="https://gist.github.com/ivanursul/5da9a51ec38d31635aa16b4522a40ad7.js"></script>

### <a href="#metricsListener" name="metricsListener"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Create Metrics Listener 

One thing you need to know about Dropwizard Metrics module is that it's required to have [MetricRegistry](http://metrics.dropwizard.io/3.1.0/apidocs/com/codahale/metrics/MetricRegistry.html) and [HealthCheckRegistry](http://metrics.dropwizard.io/3.1.0/apidocs/com/codahale/metrics/health/HealthCheckRegistry.html) classes to be instantiated under ServletContext. That's why we'll get it from constructor, and initialise context.

<script src="https://gist.github.com/ivanursul/e8301df33eebd9a2d3631db7f5cb0e8b.js"></script>

We will create a separate bean for this listener soon.

### <a href="#configclass" name="configclass"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Creating configuration class 

Now, when you have everything ready to launch your metrics, you need to inject it somewhere. I propose to do a separate configuration class for that. Let's call it **MonitoringConfiguration**

<script src="https://gist.github.com/ivanursul/2c056cfa657db78412a3aa6cfb6279fa.js"></script>

I bet, you've noticed couple of things:

* `@EnableMetrics` annotation. Yes, you need to add it, don't forget.

* **MetricsServletContextListener** initialisation. I don't have **web.xml** descriptor anymore in my app, so the true way to do it is by injecting it in spring context. Spring will take care of it.

* Extending from **MetricsConfigurerAdapter**. This class comes from [com.ryantenney.metrics:metrics-spring:3.1.2](https://www.google.com.ua/search?q=com.ryantenney.metrics%3Ametrics-spring%3A3.1.2&oq=com.ryantenney.metrics%3Ametrics-spring%3A3.1.2&aqs=chrome..69i57.248j0j7&sourceid=chrome&ie=UTF-8) dependency

* Implemented **configureReporters** method. By this method we decide what to do with our metrics. Later, in this article, I'll show you how to poll this metrics to some UI tools, which will visualise our results.

### <a href="#tunecontroller" name="tunecontroller"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Tune your controller methods 

If you want to monitor your endpoints, you need to add additional annotations to them. Add **@Timed** and **@ExceptionMetered** annotations. **@Timed** will take all the measures, while **@ExceptionMetered** will measure all exceptional situations with your endpoint.

<script src="https://gist.github.com/ivanursul/738d3af3567588139de2bfad50db6190.js"></script>

##### That's it

Now, you are ready to access your application on `http://localhost:8080/dropwizard/metrics`:

<script src="https://gist.github.com/ivanursul/ec73821fa78b61e4c7c412b504ed7a2d.js"></script>

See how more descriptive it is, comparing to what 'metrics' we have in Spring.

`AdminServlet` has another bunch of endpoints, which you can use:

* /healthcheck: HealthCheckServlet
* /metrics: MetricsServlet
* /ping: PingServlet
* /threads: ThreadDumpServlet

##### Spring power together with Dropwizard metrics <a href="#springpower" name="springpower"><i class="anchor fa fa-link" aria-hidden="true"></i></a>

Now as we have Spring here, we have the power of doing different BeanFactoryPostProcessor's for new metrics. It's much more easy, than doing it in Guice DI, which comes with Dropwizard. 

### <a href="#visualising" name="visualising"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Visualising metrics 

We can use [Graphite](https://graphiteapp.org) for that. [Here's how](https://gist.github.com/relaxdiego/7539911) you can install it on mac. For other OS's, find appropriate instuctions somewhere.

![](https://lh6.ggpht.com/-Hb-nvEzQjk8/UUndShnqRKI/AAAAAAAAQP4/tznXzGXcUE0/image_thumb%25255B2%25255D.png?imgmax=800)

Add additional dependency:

> [io.dropwizard.metrics:metrics-graphite:3.1.2](https://mvnrepository.com/artifact/io.dropwizard.metrics/metrics-graphite/3.1.2)

Then, modify our configuration class for that:

<script src="https://gist.github.com/ivanursul/b54ea49881ed1a086192214a9e0e2b0a.js"></script>

**application.properties:**

<script src="https://gist.github.com/ivanursul/6401380148075ef919667ceaf65024dc.js"></script>

### <a href="#load" name="load"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Give some load to your endpoints 

Start your Spring application, turn on Graphite on your local machine, and give some load to some of your endpoints. I prefer to do it [using load tests](http://ivanursul.com/performance-testing-explained/).

After some load, you can see, how your application behaves: I showed m1,m5 and m15 rates.

![](assets/images/Screen-Shot-2016-08-13-at-9-21-23-PM.png)

As you can see, you're now able to get more information from your application. The information you have now is much more descriptive, than you had before. I usually speak with my colleagues from different company and ask how they measure and monitor their application activity. Most of the people answer, that they sent their logs somewhere and in case of incidents, they ask their devops to get information about their app. It's a big limitation, and I think each developer should have access to metrics dashboard.

### <a href="#links" name="links"><i class="anchor fa fa-link" aria-hidden="true"></i></a> Links 

* [Dropwizard Metrics](http://metrics.dropwizard.io/3.1.0/)

* [Ryan Tenney metrics module for Spring](http://metrics.ryantenney.com/)

* [Spring Integration in Dropwizard](http://metrics.dropwizard.io/2.2.0/manual/spring/)


