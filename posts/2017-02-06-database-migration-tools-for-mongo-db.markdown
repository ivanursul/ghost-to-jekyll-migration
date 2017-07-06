---
layout: "post"
title:  "Database Migration tools for Mongo DB"
date: 2017-02-06 14:47:57
permalink: database-migration-tools-for-mongo-db
---


![](assets/images/MongoDB-Logo-5c3a7405a85675366beb3a5ec4c032348c390b3f142f5e6dddf1d78e2df5cb5c.png)

When it comes to the problem of migrating database structure, some of you may think about relational databases: there is a strict schema, and to remove something(field, table, index, etc.), you need to take action: execute an SQL statement. But when you work on schema-less databases, it may look like you don't need those migrations. But to be honest, are schema-less database [are schemaless](https://jaxenter.com/mongodb-schemaless-database-112878.html)? In fact, you have more freedom in column and document-based database, but sooner or later you will have to modify some of the results of your work: remove the index, transform column format, etc. That's why with the help of this article I would like to review the available tools for Mongo migrations.

### <a href="#mongobee" name="mongobee"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Mongobee

If you use Spring in your project, then [MongoBee](https://github.com/mongobee/mongobee) should be the most suitable tool for you. The idea is that you write Java methods(changesets), which describe what changes need to be done. The method annotated by @ChangeSet is taken and applied to the database. Mongobee stores changesets history in dbchangelog collection. If you are a Spring guy, and like Java config among others, then you should choose this tool.

You have two options how to run Mongobee - inside Spring container at the beginning of startup or as an independent process, running separately from Spring. 

The first option is good for the local environment when you have a local database and want to keep it up-to-date with shared one:

```
@Bean
public Mongobee mongobee(){
  Mongobee runner = new Mongobee("mongodb://YOUR_DB_HOST:27017/DB_NAME");
  runner.setDbName("yourDbName");  // host must be set if not set in URI
  runner.setChangeLogsScanPackage(
       "com.example.yourapp.changelogs"); // package to scan for changesets
  runner.setEnabled(true);         // optional: default is true

  return runner;
}
```

The second option is suitable for shared environments - you don't run migrations on startup, but you run them manually before deploying a new version, which requires those changes.In this case, you can run migrations in some **main** method inside a separate module:
```
  Mongobee runner = new Mongobee("mongodb://YOUR_DB_HOST:27017/DB_NAME");
  runner.setDbName("yourDbName");  // host must be set if not set in URI
  runner.setChangeLogsScanPackage(
       "com.example.yourapp.changelogs"); // package to scan for changesets
  runner.setEnabled(true);         // optional: default is true
  runner.execute();  // start migration methods
```

Then you can start writing 
```
@ChangeSet(order = "001", id = "someChangeWithoutArgs", author = "testAuthor")
public void someChange1() {
   // method without arguments can do some non-db changes
}

@ChangeSet(order = "002", id = "someChangeWithMongoDatabase", author = "testAuthor")
public void someChange2(MongoDatabase db) {
  // type: com.mongodb.client.MongoDatabase : original MongoDB driver v. 3.x, operations allowed by driver are possible
  // example: 
  MongoCollection<Document> mycollection = db.getCollection("mycollection");
  Document doc = new Document("testName", "example").append("test", "1");
  mycollection.insertOne(doc);
}

@ChangeSet(order = "003", id = "someChangeWithDb", author = "testAuthor")
public void someChange3(DB db) {
  // This is deprecated in mongo-java-driver 3.x, use MongoDatabase instead
  // type: com.mongodb.DB : original MongoDB driver v. 2.x, operations allowed by driver are possible
  // example: 
  DBCollection mycollection = db.getCollection("mycollection");
  BasicDBObject doc = new BasicDBObject().append("test", "1");
  mycollection .insert(doc);
}

@ChangeSet(order = "004", id = "someChangeWithJongo", author = "testAuthor")
public void someChange4(Jongo jongo) {
  // type: org.jongo.Jongo : Jongo driver can be used, used for simpler notation
  // example:
  MongoCollection mycollection = jongo.getCollection("mycollection");
  mycollection.insert("{test : 1}");
}

@ChangeSet(order = "005", id = "someChangeWithSpringDataTemplate", author = "testAuthor")
public void someChange5(MongoTemplate mongoTemplate) {
  // type: org.springframework.data.mongodb.core.MongoTemplate
  // Spring Data integration allows using MongoTemplate in the ChangeSet
  // example:
  mongoTemplate.save(myEntity);
}
```

**Mongobee** has a good [Wiki](https://github.com/mongobee/mongobee/wiki/How-to-use-mongobee) page, if you choose this tool, you need to read it carefully.


### <a href="#mongeez" name="mongeez"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Mongeez

[Mongeez](https://github.com/mongeez/mongeez) is something which you also can run with Spring, or without it, the only difference with Mongobee is that this tool forces you to write XML instructions, like this one:

```java
<mongoChangeLog>
    <changeSet changeId="ChangeSet-1" author="mlysaght">
        <script>
            db.organization.insert({
              "Name" : "10Gen", "Location" : "NYC", DateFounded : {"Year":2008, "Month":01, "day":01}});
            db.organization.insert({
              "Name" : "SecondMarket", "Location" : "NYC", DateFounded : {"Year":2004, "Month":5, "day":4}});
        </script>
    </changeSet>
    <changeSet changeId="ChangeSet-2" author="mlysaght">
        <script>
            db.user.insert({ "Name" : "Michael Lysaght"});
        </script>
        <script>
            db.user.insert({ "Name" : "Oleksii Iepishkin"});
        </script>
    </changeSet>
</mongoChangeLog>
```

This tool has an old XML-based config, the last commit to GitHub was [made on Dec 25, 2015](https://github.com/mongeez/mongeez/commit/7bceecaf99b0520d87d5baa690b0e498164a756c) and it looks like this is a bit older instrument than Mongobee.

These guys also have a [Wiki](https://github.com/mongeez/mongeez/wiki/How-to-use-mongeez) page describing how to set up their tool, so don't miss a chance to read it.

### <a href="#java-mongo-migrations" name="java-mongo-migrations"><i class="fa fa-link anchor" aria-hidden="true"></i></a> java-mongo-migrations
[java-mongo-migrations](https://github.com/ozwolf-software/java-mongo-migrations) is a pretty simple library and it is written on top of [Jongo](http://jongo.org/). The main idea - you write migration classes, and run them somewhere:

```
public class MyApplication {
    public void start(){
        List<MongoCommand> commands = new ArrayList<>();
        commands.add(new FirstMigration());
        commands.add(new SecondMigration());

        try {
            MongoMigrations migrations = new MongoMigrations("mongo://localhost:27017/my_application_schema");
            migrations.setSchemaVersionCollection("_my_custom_schema_version");
            migrations.migrate(commands);
        } catch (MongoMigrationsFailureException e) {
            LOGGER.error("Failed to migrate database", e);
        }
    }
}
```

I don't like to specify commands manually, and it'd be nice to specify package only. However, I like, that this tool is framework-agnostic.

### <a href="#conclusion" name="conclusion"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Conclusion


My choice - Mongobee:

* It can be framework agnostic
* I like their changeset format
* It's clear how the tool works, and Wiki has all needed information.

PS - if you have an idea to test Mongo in spring container, you should read my previous article about Spring DATA MongoDb - [Spring DATA Mongo. Testing your mongo using in-memory db](https://ivanursul.com/spring-data-mongo-testing-using-in-memory-db/)

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Is MongoDB really a “schemaless” database?](https://jaxenter.com/mongodb-schemaless-database-112878.html)