---
layout: "post"
title:  "Jenkins and Maven SQL Plugin for cleaning Postgresql database"
date: 2015-02-09 17:58:32
permalink: jenkins-ans-maven-sql-plugin-for-cleaning-database
---


How many times did you experienced issue, when you need to clean your database from unused data ?For example, you have a qa server with thousands of rows, which you don't need anymore, so it's time to delete them.
To clean up your database, someone call separate scripts for each table, someone create-drops database.I decided to delete all tables.
To remove all rows, you can use maven sql plugin, which has powerful possibilities for doing work with sql.

* First of all, if you have your maven application, I would reccomend to create separate module for sql. If you don't want to do this, just leave it as it is in your root pom.xml.

* You will need to add Maven Sql Plugin. Then, you need to map execution to install goal.

* You will need to create resource folder.This will be the place, where you will store your scripts.


#### Before we start

I don't want anyone to think, that such module is the best place for storing your database schemas creation.For this purposes there is a good framework called [liquibase](http://www.liquibase.org/) - it saves migration history and can surely know, what should be updated, and what should not.

Such module is always good for adding additional data into your dev/qa/uat/release environments.

This module can also be very useful, when you want to do some manipulations with database.


#### Maven SQL plugin

To create maven sql plugin, add plugin into **pom.xml** **build** section

<script src="https://gist.github.com/johnyUA/9e96aea61f9e5e590c5c.js"></script>

This plugin is mapped to **install** goal and will executes as soon as you will run **mvn install** command.

#### Advices

* If you have multiple modules, don't add your module into root pom.xml - just remote it from there. Such module will run each time when you will run your standard **mvn clean install** command, which will affect your database data.

* Keep database properties in your root pom.xml. I always do like that, and I can use this properties in each child module - dao, sql or migration modules.

* If you have some data insertion in your scripts - always synchronize your changes with your project version.If you have **0.9.1** version of your system - create a folder **0.9.1** with all changes from that version. That will help you a lot while working in production.


#### Conclusions

As a said before, I use this module for doing database cleaning in my project. The reason I am doint that is that I have migration scripts in liquibase, that, from time to time needs to be reupdated. Due to the fact, that liquibase stores changeset structure, next time, when I will run my updated migration script - I will receive fail. Of course, I know, that this is an issue, because I don't need to reupdate changeset - I need to write new one, but currently I am not in production - so I want to keep liquibase scripts simple.

I published jenkins job with **is-lnu-clean/pom.xml** maven pom and **mvn install** command, so when I need to clean up my database - I do that easily with my jenkins.
