---
layout: "post"
title:  "Trying new JUnit 5 - let's extend everything"
date: 2016-07-19 10:50:14
permalink: trying-new-junit-5-lets-extend-everything
---


<a href="#generalidea" name="generalidea">
#### General idea
</a>

Recently, new JUnit version has been released. I found many useful things. Besides, there're lot's of useless features. At least, I think something won't be used in new JUnit version. By the way, here the new version - [JUnit 5](http://junit.org/junit5/)

<a href="#goal" name="goal">
#### JUnit Goal
</a>

It's obvious, that JUnit decided to make their product more opensource - by releasing instruments, which will allow junit users to create lot's of [extensions](#extensions) 

<a href="#restrictions" name="restictions">
#### Restrictions
</a>

* JUnit 5 is running on java > 1.8
* JUnit 5 has lot's of new features, which are not working in JUnit 3 or 4. However, there is a project [JUnit Vintage](#howtomigrate) which allows older versions work on JUnit 5.

<a href="#storyaboutanotherpackage" name="storyaboutanotherpackage">
#### Story about another package
</a>

New version has new package - `org.junit.jupiter.*`. This was done, mostly, for separating new version from previous versions, which completely differs.

<a href="#transormations" name="transformations">
#### Transformations
</a>

All core annotation are located under `org.junit.jupiter.api` 

* **@Test** - not this annotation comes from completely new package

* **@TestFactory** - comparing to something oldes, TestFactory is a replacement of [parameterized tests](https://github.com/junit-team/junit4/wiki/parameterized-tests)

* **@BeforeEach** - new version of **@Before**. I wonder, why it was done.

* **@AfterEach** - new version of @After.

* **@BeforeAll** - replacement of **@BeforeClass**. Must be static, as usual.

* **@AfterAll** - replacement of **@AfterClass**. Must be static.

* **@Tag** - new version of **@Category**

* **@Disabled** - new **@Ignore** annotation

* **@ExtendWith** - something related to **@RunWith**, but the implementation is completely different

As you can see, JUnit made a lot of work on renaming it's annotation

<a href="#nesting" name="nesting">
#### Nested tests
</a>

Now, you can write tests classes inside your test classes. Purposes ? Don't know :)

```
@DisplayName("A stack")
class TestingAStackDemo {

    Stack<Object> stack;
    boolean isRun = false;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<Object>();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void init() {
            stack = new Stack<Object>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            Assertions.assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            Assertions.expectThrows(EmptyStackException.class, () -> stack.pop());
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            Assertions.expectThrows(EmptyStackException.class, () -> stack.peek());
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void init() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isEmpty() {
                Assertions.assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                Assertions.assertEquals(anElement, stack.pop());
                Assertions.assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                Assertions.assertEquals(anElement, stack.peek());
                Assertions.assertFalse(stack.isEmpty());
            }
        }
    }
}
```

<a href="#extensions" name="extensions">
##### Extensions instead of abstract before/after classes
</a>

The idea of extensions is to provide an abstractions for operations, which had place before or after test execution in JUnit 3,4. This allows you to move all non-business logic from before/after sections to extensions


The following interfaces define the APIs for extending tests at various points in the test execution lifecycle. Consult the following sections for examples and the Javadoc for each of these interfaces in the org.junit.jupiter.api.extension package for further details:

* [BeforeAllCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/BeforeAllCallback.html)

* [BeforeEachCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/BeforeEachCallback.html)
    
    * [BeforeTestExecutionCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/BeforeTestExecutionCallback.html)
    * [AfterTestExecutionCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/AfterTestExecutionCallback.html)

* [AfterEachCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/AfterEachCallback.html)

* [AfterAllCallback](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/AfterAllCallback.html)

<a href="#di" name="di">
##### Dependency injection. Injection test context
</a>

This version of Junit brings [dependency injection for constructors and methods](http://junit.org/junit5/docs/current/user-guide/#writing-tests-dependency-injection). [ParameterResolver](http://junit.org/junit5/docs/current/api/org/junit/jupiter/api/extension/ParameterResolver.html) is an API you should implement for your extension in order to use Dependency injection. Junit 5 ships with two pre-build parameter resolvers:

* [TestInfoParameterResolver](https://github.com/junit-team/junit5/blob/r5.0.0-M2/junit-jupiter-engine/src/main/java/org/junit/jupiter/engine/extension/TestInfoParameterResolver.java)

* [TestReporterParameterResolver](https://github.com/junit-team/junit5/blob/r5.0.0-M2/junit-jupiter-engine/src/main/java/org/junit/jupiter/engine/extension/TestReporterParameterResolver.java)

<a href="#howtomigrate" name="howtomigrate">
#### How to migrate. JUnit Vintage.
</a>

You should have junit-vintage jar in your project. Juniter Platform Launcer will automaticalyy pick up all tests then.

<a href="#links" name="links">
#### Links
</a>

* [Documentation](http://junit.org/junit5/docs/current/user-guide/)
* [Samples](https://github.com/junit-team/junit5-samples)

![](assets/images/![]()