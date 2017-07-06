---
layout: "post"
title:  "Spring XML Namespaces handling"
date: 2015-02-08 21:07:00
permalink: spring-xml-namespaces-handling
tags: java,programming
---


You all know, that spring supports xml namespaces for defining custom bean definition.

For example, if you want to set mvn annotation driven benavior, you can write such code in your xml

<script src="https://gist.github.com/ivanursul/b5ca5c3922e56e3809fc.js"></script>

As you noticed, there is no <bean ... /> in this piece of code, but there is <mvc:annotation-driven>

So, who is responsible for handling this namespaces?

Answer is: NamespaceHandler

<script src="https://gist.github.com/johnyUA/2f21efd721cab7b7419f.js"></script>

For example, here is AnnotationDrivenBeanDefinitionParser implementation.

Every Spring namespace has an associated NamespaceHandler implementation. The namespace schemas are mapped to schema files inside Spring JARs in various spring.schemas files.

The XML schema namespaces are also mapped to handler classes in spring.handlers files (several as each Spring JAR might introduce different namespaces). For your convenience here is a list of most common namespaces:

Spring core

aop - AopNamespaceHandler
c - SimpleConstructorNamespaceHandler
cache - CacheNamespaceHandler
context - ContextNamespaceHandler
jdbc - JdbcNamespaceHandler
jee - JeeNamespaceHandler
jms - JmsNamespaceHandler
lang - LangNamespaceHandler
mvc - MvcNamespaceHandler
oxm - OxmNamespaceHandler
p - SimplePropertyNamespaceHandler
task - TaskNamespaceHandler
tx - TxNamespaceHandler
util - UtilNamespaceHandler
Spring Security

security - SecurityNamespaceHandler
oauth - OAuthSecurityNamespaceHandler
Spring integration

int - IntegrationNamespaceHandler
amqp - AmqpNamespaceHandler
event - EventNamespaceHandler
feed - FeedNamespaceHandler
file - FileNamespaceHandler
ftp - FtpNamespaceHandler
gemfire - GemfireIntegrationNamespaceHandler
groovy - GroovyNamespaceHandler
http - HttpNamespaceHandler
ip - IpNamespaceHandler
jdbc - JdbcNamespaceHandler
jms - JmsNamespaceHandler
jmx - JmxNamespaceHandler
mail - MailNamespaceHandler
redis - RedisNamespaceHandler
rmi - RmiNamespaceHandler
script - ScriptNamespaceHandler
security - IntegrationSecurityNamespaceHandler
sftp - SftpNamespaceHandler
stream - StreamNamespaceHandler
twitter - TwitterNamespaceHandler
ws - WsNamespaceHandler
xml - IntegrationXmlNamespaceHandler
xmpp - XmppNamespaceHandler