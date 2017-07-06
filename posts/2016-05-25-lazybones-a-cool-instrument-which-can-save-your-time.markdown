---
layout: "post"
title:  "Lazybones - a cool instrument, which can save your time during a project setup"
date: 2016-05-25 14:03:04
permalink: lazybones-a-cool-instrument-which-can-save-your-time
---


How long does it takes for you to create a new project ? And how often are you doing this routine job ? Do you remember, how interesting for you is to test some feature, which you want to test/try, but how hard is it to configure initial project ? 
There's so many frameworks in java world, and for me it's extremely hard to remember initial steps and configurations for each of them. And logically, if you do al this teps by your hands, then something can potencially go wrong
![alt](http://i.imgur.com/8eYyobe.gif)

Let's start with a question "Why you need to create a new project":

* You'd like to test something in your favorite project.
* You're working in a microserviced worlds, and you start new microservice every day...
* You need to create a POC and test how it works.
* Anything else

Personally I don't want to store all this configuration steps in my head, and I prefer to use something, which can the work for me. That's why I'd like to recommend a cool instrument for creating project skeletons in extremely convenient way - lazybones. Let's demonstrate how to setup a [Gatling](http://gatling.io/) project

	lazybones create https://bintray.com/ivanursul/lazybones-templates/download_file\?file_path\=gatling-template-0.1.zip load-tests
    
Quite easy, don't you find ? Especially, when you can set your own templates, and tune them, add README.md, custom folders, create folders, etc.
![alt](http://i.imgur.com/OX4eZnQ.gif)

Lazybones was born out of frustration that [Ratpack](https://ratpack.io)
does not and will not have a command line tool that will bootstrap a project.

I recommend to review their [README.md](https://github.com/pledbrook/lazybones/blob/master/README.md) to understand all the features, my article is just an overview on how I understand this instument.

######How to install lazybones

* Install [sdkman](http://sdkman.io/)
* Install lazybones 
	`sdk install lazybones`

###### How to use it

Let's create a [Ratpack](https://ratpack.io) project 
`lazybones create ratpack 1.2.0 my-rat-app`
You will get intro text about what this template is about.
![alt](/content/images/2016/05/Screen-Shot-2016-05-25-at-1-16-50-PM.png)

I think it's a true way of starting your new project with README for some technology, because you can coordinate yourself with a steps you need to do with this project.

#####Available templates
By the time I find this instument, there was not so many useful templates for me. To see, what templates are available, just type

	lazybones list
    
This command will list all the templates, which are available for now. You can read [more](https://github.com/pledbrook/lazybones#finding-out-what-templates-are-available) about available templates on github. It's also not so clear for me, how to list all custom templates, which users create for themselves, and push to bintray. You can read how to import custom repos [here](https://github.com/pledbrook/lazybones#the-project-templates)


#####Cook templates for yourself
Complete document on how to create custom templates is located on [github](https://github.com/pledbrook/lazybones#the-project-templates)
I'll describe how I understand it:

* Create account on [bintray.com](https://bintray.com/)
* Get apiKey from settings page.
* Create lazybones template project from lazybones template(we're lazy, don't forget it)
	`lazybones create lazybones-project my-lzb-templates`
* Create a folder in templates folder. E.g - myTemplate
* Build your folder according to [this](https://github.com/pledbrook/lazybones/wiki/Template-developers-guide#creating-a-template) document
* `cd my-lzb-templates`
* `./gradlew installAllTemplates`
* `lazybones create myTemplate 1.0-SNAPSHOT my-new-project`
