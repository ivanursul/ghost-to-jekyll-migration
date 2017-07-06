---
layout: "post"
title:  "Spring DATA  Mongo. Testing your  mongo using in-memory db"
date: 2016-06-10 17:46:02
permalink: spring-data-mongo-testing-using-in-memory-db
---


######Embeddable
When I was a bit younger, I couldn't understand why more senior engineers stood for embedding builds as much as it's possible. For instance, you have a database, and you're running your integration tests only with in memory databases. It was very unusual for me, why having a working database on your local machine, you're using some weird in memory things ? The time have passed, and I understand now, that embedding your builds is a good practice, because:

* It reduce your build time
* It decouples your build phase from any environments
* Even if you don't have any database/thirdparty tool installed on your local machine, your build will finish successfully, and after then you can start installing all required third party instruments.

###### Fongo + NoSQl-Unit

By this article I'd like to show how to effectively test your Spring DATA repositories using [Fongo](https://github.com/foursquare/fongo) - an in-memory implementation.

I'm not going to explain how Spring Data works, you can read their documentation [here](http://docs.spring.io/spring-data/commons/docs/current/reference/html/)

Say, you have following repository

```
package org.example.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.startup.queue.domain.Establishment;

import java.util.Optional;

public interface SomethingRepository extends MongoRepository<Establishment, String> {

    Optional<Something> findByCol1(String col1);

}
```

Next, test it:

```
package org.example.repository;

import com.github.fakemongo.Fongo;
import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.Mongo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.startup.queue.domain.Establishment;
import org.startup.queue.domain.Table;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SomethingRepositoryTest {

    // Don't forget to add this field
    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("demo-test");

    @Autowired
    private SomethingRepository unit;

    @Test
    @UsingDataSet(locations = "somethings.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void testFindByTablesQr() throws Exception {
        // Given
        Something expected = new Something();
        ... data from somethings.json

        // When
        Optional<Something> actual = unit.findByCol1(col1);

        // Then
        assertEquals(expected, actual.get());
    }


    @Configuration
    @EnableMongoRepositories
    @ComponentScan(basePackageClasses = {SomethingRepository.class})
    static class SomethingRepositoryConfiguration extends AbstractMongoConfiguration {


        @Override
        protected String getDatabaseName() {
            return "demo-test";
        }

        @Bean
        public Mongo mongo() {
            Fongo queued = new Fongo("something");
            return queued.getMongo();
        }

        @Override
        protected String getMappingBasePackage() {
            return "org.startup.queue.repository";
        }

    }
}
```

Let's explain:

```
    @Autowired
    private ApplicationContext applicationContext;
```
By this line you are forcing Spring Context to fully load under this class. If you'll skip this line - `mongoDbRule` won't work.

```
    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("demo-test");
```

By adding this line you're making sure, that you can use NoSQL-Unit. I mean, adding this line will allow you to use
`@UsingDataSet` annotation. Of course, you can use **Fongo** without NoSQL-Unit. In this case you will need to manually add records into your nosql storage. Refer to github of following project to understand all possibilities of this project.


```
    @UsingDataSet(locations = "somethings.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
```

By line above you are loading your collection data into your in memory database.
PS - your something.json should be under `src/test/resources/org/example/repository/something.json` folder.


```
        @Bean
        public Mongo mongo() {
            Fongo queued = new Fongo("something");
            return queued.getMongo();
        }
```
How your in memory database is being created ? You need to override spring bean, which stands for creating Mongo object.

That's all what you need to do to test your mongo repository. I've noticed, that testing Spring Data using in-container mode is the only reasonable way to test them.

###### Links
* https://github.com/lordofthejars/nosql-unit
* https://github.com/foursquare/fongo
