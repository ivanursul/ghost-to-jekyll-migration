---
layout: "post"
title:  "Spring XML and beans inheritance"
date: 2015-02-08 00:00:37
permalink: spring-xml-and-beans-inheritance
tags: java,programming
---


If you are using XML configuration in your Spring application, you should know about gorgeous feature, that spring has: bean inheritance. What does this mean ? This mean, that you can create an abstract bean with some predefined properties, and later, you will be able to make an inheritance from this bean.

Here is an example

<script src="https://gist.github.com/johnyUA/0c991f4c0490fd409194.js"></script>

Here you have DefaultDao implementation, which has three fields - persistenceManager, which is the same in every Dao class, queryBuilder for building dynamic queries,  which are dynamic field, and entityClass, which is the name of Class, that will be mapped to result from database.

And here we have xml context, that we use without bean inheritance.

<script src="https://gist.github.com/johnyUA/fdf6a2ed1c9b11580584.js"></script>

After some modification and refactoring we will receive

<script src="https://gist.github.com/johnyUA/70a5ad7ce5555d920d5c.js"></script>

As you can see, we reduce number of lines in context and now we are instantiating persistenceManager from one place and if we will want to change it, we will do that very quickly.