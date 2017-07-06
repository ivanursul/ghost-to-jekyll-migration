---
layout: "post"
title:  "How to work with maven javacc plugin"
date: 2015-10-23 08:45:19
permalink: how-to-work-with-maven-javacc-plugin
---


I had to do some work with google-visualization-java github project.
I needed to change some logic for correct google visualization query.
I found, that guys were using **javacc** compiler.

Official site says: Java Compiler Compiler tm (JavaCC tm) is the most popular parser generator for use with Java tm applications. A parser generator is a tool that reads a grammar specification and converts it to a Java program that can recognize matches to the grammar. In addition to the parser generator itself, JavaCC provides other standard capabilities related to parser generation such as tree building (via a tool called JJTree included with JavaCC), actions, debugging, etc.

To inject javacc inside your maven project, add following plugin

```
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>javacc-maven-plugin</artifactId>
        <version>2.5</version>
        <executions>
          <execution>
            <id>javacc</id>
            <goals>
              <goal>javacc</goal>
            </goals>
            <configuration>
              <sourceDirectory>${basedir}/src/main/java</sourceDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

Steps you need to do with your javacc project:

* Open terminal
* cd to project
* mvn clean generate-sources

That's it.