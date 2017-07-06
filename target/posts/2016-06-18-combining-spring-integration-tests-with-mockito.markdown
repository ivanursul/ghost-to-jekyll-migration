---
layout: "post"
title:  "Combining Spring Integration Testing with Mockito"
date: 2016-06-18 16:51:58
permalink: combining-spring-integration-tests-with-mockito
tags: java,junit,spring-framework,integration testing,mockito
---


Integration tests...they are perfect for testing your data flows. You send some request to your application and can control how data is being processed throughout your application. You see how request is received by your controller, then it's sent to service, dao or other layers, that you have in your application. Sometimes you don't want some layer to do real work in Spring. For instance, your dao layer is using some native queries to get data from database, and some embedded database doesn't support some query syntax. Naturally, you still want to have your integration tests, but without a real call to database. What can we do in this situation ? I'd suggest to mock this dao layer, using mockito. Let's demonstrate how it works ?

######Project setup
I use [Spring Initialzr](https://start.spring.io) to setup projects, so let's create a simple Spring Boot application. Code can be found [here](https://github.com/ivanursul/spring-integration-mockito).

######Project structure
```
├── README.md
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── spring-integration-mockito.iml
└── src
    ├── main
    │   ├── java
    │   │   └── org
    │   │       └── springmockito
    │   │           └── demo
    │   │               ├── DemoApplication.java
    │   │               ├── ExampleDao.java
    │   │               ├── ExampleEntity.java
    │   │               ├── ExampleService.java
    │   │               └── TestConfiguration.java
    │   └── resources
    │       └── application.properties
    └── test
        └── java
            └── org
                └── springmockito
                    └── demo
                        └── ExampleServiceTest.java
```

Let's try to explain step by step how it should work

######Roles

* **ExampleDao** - class, which will be mocked. Let's say, ExampleDao is doing very hard database operation, and we don't want to waste time on it. Instead of waiting, you can mock it.

* **ExampleService** - class, which will have **ExampleDao** as a field. Nothing special, just some delegation to Dao layer.

* **ExampleServiceTest** - place, where we will use Spring with Mockito.

```

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class ExampleServiceTest {

    @Autowired
    private ExampleDao exampleDao;

    @Autowired
    private ExampleService unit;

    @Test
    public void testGetEntity() throws Exception {
        // Given
        Long id = 1L;
        ExampleEntity expected = new ExampleEntity(1L, "Mocked name");

        // When
        doReturn(expected)
                .when(exampleDao).longRunnintGetById(id);

        ExampleEntity actual = unit.getEntity(id);

        // Then
        assertEquals(expected, actual);
    }
}
```

If I were you, I'd have two questions: why exampleDao is missing annotation `@Mock` and what is `@ActiveProfiles("test")`Because we are createa mock using Spring dependency injection, we don't need to annotate our mock field with any @Mock annotations.

* **TestConfiguration** - test configuration, which has one method of ExampleDao returning type. Notice, how it's returned.

```
    @Bean
    public ExampleDao exampleDao() {
        return mock(ExampleDao.class);
    }
```

**TestConfiguration** also has `@Profile("test")` annotation, which is an indicator, that this configuration should run only on test profile. `@Profile`, together with `@ActiveProfiles` are loading **TestConfiguration** inside our test. That's why ExampleDao is annotated as a spring bean, because we need to get it from context.