---
layout: "post"
title:  "Why we need to delegate some work to database."
date: 2015-02-21 01:27:46
permalink: why-we-need-to-delegate-some-work-to-database
---


Before writing this post what I want to underscore is that this port is mostly for middle layer developers, that don't now many things about database.
Around year ago I started to work on some freelance project.I tried to use all up-to-date technologies: Cool web framework Spring with DI(Dependency Injection), ORM framework by **java persistence api(JPA)** specification, simple MySQL database. I decided to make RESTful service, and, in the end, I had 20+ methods there.I finished my part of project and delegated another one to other people. The reason I am writing this post is that application had one BIG ISSUE, that I understood recently: It did most of the db logic in the middle layer. Yes, all compatibility checks, some data validation and other staff were done in my web service. I would like to emphasize why It is bad:

There is a chance that somone will use your database beyond your application, for example, someone will insert invalid row.Then there is a big risk that your application will broke it's constistency - some data will now be invalid or broken. That's why I make a conclusion that when you are choosing where to store your logic - in the database or in application - think about consistency of your database without your middle - layer application.

I can show real life example: I have a database with 130+ tables, where one of such tables is table about persons - all the persons, with plenty of personTypes - students, teachers, professors, etc.

<script src="https://gist.github.com/johnyUA/e2ac4aff7ac089b057aa.js"></script>

My desire behavior is that when user updates **personTypeId** row to some different from existing value  - then there should be new row with new updated data, but there also should be old one row.

How can we achieve this ? Of course, you have your web application written on java/.net/python/php and you can do additional check if **personTypeId** is different from value in database - then you can make new insertion instead of update. But as I said before, this method brokes your consistency.

To be abosultely sure that your data will keep consistent state, you can make your logic on database by adding triggers.I position myself as java developer, and have very poor experience with database. Thanks to other db developers, I got database with well-designed schema.Finally, I got advices from my coworker Nazar with instructions on how to use triggers.
So, here is algorithm of work

* We will create trigger, that will triggers update **operations**

* if new personTypeId will be different from old one, then we will insert new row into our database with new updated data and we will **not** update old row.

Here you can see sql script

<script src="https://gist.github.com/johnyUA/4326301b16739d52701b.js"></script>

As you can see, trigger will fire, when there will be update action on row, and in case if personTypeId will de different, there will a new row, but old row will be not updated.Also, there are some operations with remapping required relations on person additional tables.

The only problem, that I see here is that if user will update row - he will receive old one with no information about location of new inserted row. If someones know, how to deal with such problem - I will be happy to read your suggestions.

I would like to conclude by saying, that this post is just my impression on new approach, and if there is some troubles here - feel free to contact me.

