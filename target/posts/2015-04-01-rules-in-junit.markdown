---
layout: "post"
title:  "Writing JUnit Rules"
date: 2015-04-01 18:23:30
permalink: rules-in-junit
tags: java,programming
---


## Agenda

</br>


* Description of rules
* **@Rule** vs **@ClassRule**
* Some useful rules
* Writing your own rule

##### Description of rules

Rules allow very flexible addition or redefinition of the behavior of each test method in a test class. Testers can reuse or extend one of the provided Rules below, or write their own.

This mean, that you can controll the behavior of your test case, you can catch exceptions, write before-after methods, etc.

I would recommend to read more about rules here
https://github.com/junit-team/junit/wiki/Rules

##### @Rule vs @ClassRule

There is a difference between **@Rule** and **@ClassRule** in JUnit.We can differ them similiar as **@Before** and **@BeforeClass**. If you will annotate field ,that implements **org.junit.rules.TestRule** with **@Rule** annotation, then it will run in each method. But, if you will annotate this field as **@ClassRule**, then it will run once.

##### Useful rules examples

<script src="https://gist.github.com/johnyUA/bac0bac583210d52d5fa.js"></script>

##### Writing your own rule

We will write custom **TestRule**, that will log actions. We will have two 

<script src="https://gist.github.com/johnyUA/15a96f98ddf4ecb01fd1.js"></script>


<script src="https://gist.github.com/johnyUA/47876fc4c56646ddcaa8.js"></script>

We will get

    before: global
    before: local
    after: local
    after: global
