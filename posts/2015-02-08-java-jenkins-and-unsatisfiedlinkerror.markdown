---
layout: "post"
title:  "Java, Jenkins and UnsatisfiedLinkError"
date: 2015-02-08 00:23:32
permalink: java-jenkins-and-unsatisfiedlinkerror
---


Today I experienced issue with Jenkins - After some system modifitcation I received failed build with message

> Caused by: java.lang.UnsatisfiedLinkError: > /home/${user}/tools/jdk1.7.0_71/jre/lib/i386/xawt/libmawt.so: libXrender.so.1: cannot open shared object file: No such file or directory

So I search in the internet and found the solution

> sudo apt-get install libxrender-dev

> sudo apt-get install libxtst-dev

I'm really not sure what it was, I guess it could be because I accidentally installed openjdk instead of oracle jdk.

In fact, It helped me.