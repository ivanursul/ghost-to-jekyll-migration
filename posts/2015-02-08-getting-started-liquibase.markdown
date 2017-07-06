---
layout: "post"
title:  "Getting in touch with Liquibase Migration Tool"
date: 2015-02-08 09:24:04
permalink: getting-started-liquibase
---


Have you experienced a situation, when you had a database in your system, and you needed to migrate all it's structure, to, say, another database ? Of course, you faced such situations and, of course, you manually copied all data from one server instance to another ? Something familiar ?

#### Alternative approach

How about idea of writing high-level instructions on how to deal with your database ? For example, you say "Create this table, and add this column , and insert this index, ..." only once and all subsequent times you will just reuse this instructions ?

Such idea is called "Database migration tool", there is several migration frameworks for java, but I will show how to work with single tool - liquibase.

The idea is that you store this instructions in xml files and when you run your liquibase migration script for the first time, liquibase will create two tables in your database  databasechangelog and databasechangeloglock, where all information about running migrations kept.

Say, you have only one migration changeset

<script src="https://gist.github.com/johnyUA/a88227a4917741539e23.js"></script>

To run such script I use maven, here is pom.xml,as usual

<script src="https://gist.github.com/johnyUA/22da0b907b1ca7dca765.js"></script>

> mvn liquibase:update

If you wish, you can run liquibase from command line, the only thing you need is to set required parameters

> java -jar liquibase.jar --driver=com.mysql.jdbc.Driver \
     --classpath=/path/to/classes \
     --changeLogFile=com/example/db.changelog.xml \
     --url="jdbc:mysql://localhost/example" \
     --username=user \
     --password=asdf \
     update
     
#### Must know

Liquibase migrations are running top-down, so consider writing your scripts  knowing this fact. This is important. For example, if you have tableA with foreign key of tableB, and table B with foreignKey of tableA, in case of ignorance if this fact you will write your migration with following way

<script src="https://gist.github.com/johnyUA/d3fca83fbe8c35efb11d.js"></script>

And this migration will fail, because during the execution of first migration there will be no tableB.
To Solve this sort of issue you can do

<script src="https://gist.github.com/johnyUA/52a88ca4c33ab2066830.js"></script>

Each migration unit is being executed in transaction mode. That mean, that this unit will execute fully without exceptions, or will not be executed at all.
You can generate migrations from designed database - just execute

> mvn liquibase:generateChangeLog

#### Suggestions

Here is some suggestions on how to use liquibase:

You split your liquibase migrations into different parts
Each global part should support appropriate version of your system.Say, you have version 1.0, 1.1 , and 1.2, so there should be 3 different folders with version 1.0, 1.1 and 1.2. Pay attention, that after executing folder with version 1.2 your system should have all the changes from version 1.0 and 1.1.
Each folder with version should have a cumulative file, which will import all migration files from that folder(data structure migrations, data migrations, etc...)
Follow your own conventions, as usual. If you decided to have some default structure of your migrations, then support this structure.
Conclusions

I am using liquibase for around 1 year and during this time I can confidently say, that this is very poweful tool. It can simplify your life, you will forget about problems with synchronizing your database versions.