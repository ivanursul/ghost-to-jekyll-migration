---
layout: "post"
title:  "Better application deployment. Monitoring your application with Graphite/Grafana"
date: 2016-12-05 21:59:36
permalink: better-application-deployment-monitoring-your-application-with-graphite-grafana
tags: ansible,terraform,java,spring boot,digitalocean,devops
---


### <a href="#intro" name="intro"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Intro

In the [previous part](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-connecting-terraform-with-ansible/) I explained how to use Terraform, Ansible, Docker and Spring Boot to deploy applications in the cloud. Today I'd like to introduce something, which will work as a monitoring tool, inside our infrastructure. If you follow my blog posts, you should remember a post about [Spring and Dropwizard Module](https://ivanursul.com/monitoring-your-spring-application-using-dropwizard-metrics-module/) - there I explained how you could get a meaningful metrics from your app. 

But wait, why you should even do monitoring and can you skip this part? Well, when I first came to the project, when a wide variety of metrics was present in each of the microservices in the ecosystem, I had a feeling that this is something which I won't use in the future. I was right, and I didn't use them...until my first incident, on which I had to understand what's going on. I start looking for some explanations, and found that our service is sending many 500 statuses. Then I found out, that one dependant service, which we use to get some part of response, is broken, and problem is not on our side.

From that period I introduced a couple of custom dashboards, and during incidents/crashes, I can answer most of the questions about what's going on by just opening my monitoring dashboards. Sometimes, I need to prove my assumption by reading logs, and this is a different story, and I'll cover it later. Шf you're reading this blog post and don't have a monitoring system on your project, now you have enough arguments on having some. There're many options on the market, and you're free to choose. 

For this blog post I decided to use [Graphite](https://graphiteapp.org/)/[Grafana](http://grafana.org/) couple, which will be running on [DigitalOcean](https://www.digitalocean.com/) cloud, created by [Terraform](https://www.terraform.io/), and configured by [Ansible](https://www.ansible.com/).

![](assets/images/out1.gif)

### <a href="#exposehttp" name="exposehttp"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Turning on metrics in Spring

I encourage you to read [Monitoring your Spring application using Dropwizard metrics module](https://ivanursul.com/monitoring-your-spring-application-using-dropwizard-metrics-module) article, there's a deep explanation on how to, in this article I'll cover the basics, without step-by-step explanation.

Create **MonitoringConfiguration** class:

```
package org.ivanursul.terraform.ansible;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;
import com.codahale.metrics.graphite.GraphiteReporter.Builder;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.servlets.AdminServlet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableMetrics
@ConditionalOnProperty(name = "metrics.enabled", havingValue = "true")
public class MonitoringConfiguration extends MetricsConfigurerAdapter {

    @Value("${graphite.host}")
    private String graphiteHost;

    @Value("${graphite.port}")
    private int graphitePort;

    @Value("${graphite.amount.of.time.between.polls}")
    private long graphiteAmountOfTimeBetweenPolls;

    @Autowired
    private MetricRegistry metricRegistry;

    @Autowired
    private HealthCheckRegistry healthCheckRegistry;

    @Autowired
    private Graphite graphite;

    @PostConstruct
    public void init() {
        configureReporters(metricRegistry);
    }

    @Bean
    public MetricsServletContextListener metricsServletContextListener(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry) {
        return new MetricsServletContextListener(metricRegistry, healthCheckRegistry);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        return new ServletRegistrationBean(new AdminServlet(),"/dropwizard/*");
    }

    @Bean
    public Graphite graphite() {
        return new Graphite(
                new InetSocketAddress(graphiteHost, graphitePort)
        );
    }

    @Bean
    @ConditionalOnProperty(name = { "graphite.enabled", "metrics.enabled"}, havingValue = "true")
    public GraphiteReporter graphiteReporter(Graphite graphite) {
        GraphiteReporter graphiteReporter = getGraphiteReporterBuilder(metricRegistry).build(graphite);
        registerReporter(graphiteReporter);
        graphiteReporter.start(graphiteAmountOfTimeBetweenPolls, TimeUnit.MILLISECONDS);

        return graphiteReporter;
    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {
        registerReporter(JmxReporter.forRegistry(metricRegistry).build()).start();
    }

    private Builder getGraphiteReporterBuilder(MetricRegistry metricRegistry) {
        metricRegistry.register("gc", new GarbageCollectorMetricSet());
        metricRegistry.register("memory", new MemoryUsageGaugeSet());
        metricRegistry.register("threads", new ThreadStatesGaugeSet());
        return GraphiteReporter.forRegistry(metricRegistry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .filter(MetricFilter.ALL);
    }

}
```

And this one, **MetricsServletContextListener**:

```
package org.ivanursul.terraform.ansible;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MetricsServletContextListener implements ServletContextListener {

    private MetricRegistry metricRegistry;
    private HealthCheckRegistry healthCheckRegistry = new HealthCheckRegistry();

    public MetricsServletContextListener(MetricRegistry metricRegistry, HealthCheckRegistry healthCheckRegistry) {
        this.metricRegistry = metricRegistry;
        this.healthCheckRegistry = healthCheckRegistry;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,healthCheckRegistry);
        servletContextEvent.getServletContext().setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
```

You also need to update your **application.properties** file:

```
metrics.enabled=true
graphite.enabled=false
graphite.host=localhost
graphite.port=2003
graphite.amount.of.time.between.polls=20000
```

Here we set **graphite.enabled** to false, since we don't want to corrupt things on localhost. When we set graphite/grafana connection, we will be able to specify what host do we have on the cloud, and then we will enable graphite.

PS - You can, of course, try for yourself, and enable it on localhost, but before you need to install graphite. [Here's](https://gist.github.com/relaxdiego/7539911) a brief Github Gist on how to install it.

Just setting proper metrics and exposing them to HTTP endpoint is not enough. In our case, we need to send this metric data to Graphite. That's why we need to create an instance of a class called [**GraphiteReporter**](http://metrics.dropwizard.io/3.1.0/apidocs/com/codahale/metrics/graphite/GraphiteReporter.html) - everything you need to set is graphite host, port and polling pause.
 

### <a href="#monitoringinstance" name="monitoringinstance"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Additional cloud instance for Grafana

Things are getting interesting, aren't they? We did all Java work, and now we need to send our metrics to Graphite and show them on Grafana. I decided to create a separate instance because in case of failures and system shutdowns I'll be able to open the dashboard and see, what's happening. And besides, the more general rule of thumb is that we should use separate machines for each unit in our architecture. Separation of concerns. Everything should be on its place.

![](assets/images/ScholarlySolidGermanshepherd.gif)

So, let's create terraform resource - an instruction for additional cloud instance. Please revise [previous](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-connecting-terraform-with-ansible/) article for understanding things.

```
...
resource "digitalocean_droplet" "app-monitoring" {
  image = "ubuntu-14-04-x64"
  name = "app-monitoring"
  region = "${var.region}"
  size = "512mb"
  ssh_keys = ["${var.ssh_fingerprint}"]
}
```
Full terraform file can be found on [github-ADDD-LINk].(...)
Creating instance...

```
terraform apply \  
-var "token=${DIGITALOCEAN_ACCESS_TOKEN}" \
-var "pub_key=$HOME/.ssh/id_rsa_do_token.pub" \
-var "pvt_key=$HOME/.ssh/id_rsa_do_token" \
-var "ssh_fingerprint=${SSH_FINGERPRINT}"
```

Ok, we've checked out the cloud, we see a new instance, there's nothing new comparing to previous parts, so we can start configuring it.

### <a href="#playbook" name="playbook"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Playbook for setting Graphite/Grafana

Finally, we can start doing something entirely new - installing Graphite and Grafana on our instance.

We should install Graphite first, and then Grafana. Graphite should be configured together with Carbon. Carbon is one of three components within Graphite project.

![](assets/images/overview.png)

The idea is that we collect metrics from Java application, and then send them to carbon, which processes these metrics. Then there is a graphite-web app, which can render and show metrics, but user interface, which is present there, could be better. Additionally, there are no features, which helps us to understand graphs. That's why we use Grafana - for its excellent user interface and bunch of functions.

![](assets/images/diagram-1.svg)

You see a diagram with two databases. However, there'll be a single database, with tables from both Graphite and Grafana. If you need two databases, feel free to tune ansible script and create an additional database.

All scripts located on GitHub project, and you can observe them [here](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/tree/2bc1b501fe1603d3ff9f3f157810fc28e0506882)

So, what's ansible doing on **app-monitoring** instance?

* Installs all Graphite, including Carbon. Carbon runs on **2003** port and Graphite is on **8080**
* Installs postgres database
* Installs Grafana, on **8567** port. Credentials **admin:SecureAdminPass**
* Deploys Spring Boot application with graphite host [param](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/blob/2bc1b501fe1603d3ff9f3f157810fc28e0506882/infrastructure/playbook-deploy.yml#L19)

Steps I did should be enough for Graphite/Grafana, we've configured Spring Boot o send metrics to Carbon, and added data source for Grafana, so we have monitoring dashboards, which works out of the box.

Run following script 
```
TF_STATE=terraform.tfstate ansible-playbook --inventory-file=/usr/local/bin/terraform-inventory playbook-bootstrap.yml
``` 

Posting a little video which shows how to deploy and start everything from this article.

<div style="text-align: center;"><iframe width="560" height="315" src="https://www.youtube.com/embed/zHxHgZE1svU" frameborder="0" allowfullscreen></iframe></div>

### <a href="#links" name="links"><i class="centerme fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Better application deployment with DigitalOcean, Terraform, Ansible, and Docker. Creating basic terraform instances](https://ivanursul.com/better-application-deployment-with-terraform-ansible-and-docker-part-1/)
* [Better application deployment with DigitalOcean, Terraform, Ansible and Docker. DNS Records](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/)
* [Better application deployment with DigitalOcean, Terraform, Ansible and Docker. Connecting Terraform with Ansible.](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-connecting-terraform-with-ansible/)
* [Monitoring your Spring application using Dropwizard metrics module](https://ivanursul.com/monitoring-your-spring-application-using-dropwizard-metrics-module/)
* [Carbon project](https://github.com/graphite-project/carbon)