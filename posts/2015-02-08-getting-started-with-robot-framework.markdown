---
layout: "post"
title:  "Getting started with Robot Framework"
date: 2015-02-08 21:05:17
permalink: getting-started-with-robot-framework
---


Robot Framework is a generic test automation framework for acceptance testing and acceptance test-driven development (ATDD). It has easy-to-use tabular test data syntax and it utilizes the keyword-driven testing approach. Its testing capabilities can be extended by test libraries implemented either with Python or Java, and users can create new higher-level keywords from existing ones using the same syntax that is used for creating test cases.

Robot Framework is operating system and application independent. The core framework is implemented using Python and runs also on Jython (JVM) and IronPython (.NET).

We will be using Robot Framework with Java + Maven build tool.

Need to have

* Java. I use java 1.7
* Maven. I use Maven 3.2.2 version
* Downloaded SeleniumLibrary.
* Willings to start something new and interesting :)
* Preparations

All you gonna need to start using Robot Framework is his SeleniumLibrary, please, download it.

So, just create a simple maven project with jar packaging.

Open your project object model file - pom.xml and add Robot Framework plugin -

<script src="https://gist.github.com/johnyUA/86f46698f93f81e229bd.js"></script>

After that, create two folders

* src/test/robotframework/acceptance
* src/test/resources/robotframework/libraries

Then go to your downloaded library folder, find robotframework-seleniumlibrary-2.9.1.tar.gz, extract this tar archieve, open, go to src folder and copy

robotframework-seleniumlibrary-2.9.1/src/SeleniumLibrary to

{project folder}/src/test/resources/robotframework/libraries/SeleniumLibrary

You should have similary structure:

![](assets/images/Screen-Shot-2015-01-01-at-1-06-19-PM-139x300.png)

 

Now,  you have all required configuration and the only thing that is left to do is to create appropriate test cases in your project.

Go to /src/test/robotframework/acceptance and create BasicTest.txt

<script src="https://gist.github.com/johnyUA/1d8e79bec743e993c12e.js"></script>

 

Then open your terminal, navigate to your project folder and type

> mvn verify


Also, you can find generated robot folder in your project target folder and open reports.html

As you can see, the test is quite simple, without any hard logic, just to demonstrate how to work with this framework.

This example has it's [own github page](https://github.com/johnyUA/robot-framework-getting-started)