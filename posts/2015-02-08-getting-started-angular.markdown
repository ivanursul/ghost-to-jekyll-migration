---
layout: "post"
title:  "Develop your Angular.js application with Yeoman, Grunt and Bower"
date: 2015-02-08 09:40:39
permalink: getting-started-angular
---


I position myself as backend developer, but I truly undestand, that I should know more about frontend world. So, by this post, I will start investigating frontend world.

This time I will try angular.js - popular framework for building responsive web applications.

Notice, that raw angular.js withour any thirdparty libraries is not so powerful, so I will be using Yeoman, Grunt and Bower in this article for convenient work.

#### Expectation

This tutorial will cover:

* Generating of bare angular.js project.
* Use Grunt to make testing
* Use Grunt to deploy application
* Use Bower to add thirdparty plugins
* Making some little changes to our application.

I assume that you have already installed npm package manager.

#### Prerequisites

To be confident during this tutorial I recommend you to have the following skills and resources available:

Basic knowledge of command line
node.js and npm
Knowledge of HTML, CSS, JS
Ready, and tested Vanilla JS framework :D (Just joke)
Resources

You can find this project on his GitHub page

#### Installation

Before installation, I should say some words about setting up npm. When I first downloaded and installed npm on OS X and tried to configure project from various tutorials - I received plenty of permission exceptions in my terminal. So, I recommend to make changes, mentioned below in the link

Fixing npm permissions

After that, you can start.In terminal, run:

> npm install -g yo grunt-cli bower

This command should install yo, grunt and bower. Right now, we have a powerful set of instruments, that will help us to build our demo application.

I will write couple of words about each framework.

#### Yeoman

Yeoman helps you to kickstart new projects, prescribing best practices and tools to help you stay productive.

You should read more on their official site - yeoman.io

#### YO

Yo is a CLI too for running yeoman generators

Github page - github.com/yeoman/yo

#### Bower

Bower works by fetching and installing packages from all over, taking care of hunting, finding, downloading, and saving the stuff you’re looking for. Bower keeps track of these packages in a manifest file, bower.json. How you use packages is up to you. Bower provides hooks to facilitate using packages in your tools and workflows.

Official site - bower.io

One of the best features of Yeoman is the ability to use custom generators. We’re going to intall the AngularJS generator to help us get up and running with Angular as quick as possible.

Run the following to install the AngularJS generator:

> npm install -g generator-angular

> bower install angular-bootstrap --save

Now it’s time to generate a shiny new AngularJS application. In a fresh project directory, run:

#### yo angular

Generator will ask you couple of questions, I choosed Bootstrap and accept default settings for packages.

##### What is generated?

Let’s take a look at what Yeoman’s given us:

* .bowerrc
* .editorconfig
* .gitattributes
* .gitignore
* .jshintrc
* Gruntfile.js
* app/
* bower.json
* node_modules/
* package.json
* test/
* test/karma.conf.js

Let’s go over some of the more important things to notice here:

###### app/ directory
The app directory contains your static app. It has your html, css and javascript in it and it’s where you’ll spend most of your time developing.

###### package.json

The package.json file helps npm to identify our project as well as to manage all of it’s dependencies. It can also contain all sorts of other metadata relevant to your project.

###### node_modules

This one is self explanatory. This is where all the node modules that your project depends on are stored.
###### Gruntfile.js
The Gruntfile is a javascript file that is responsible for configuring your project as well as any tasks or plugins that your project requires. For instance, your gruntfile might specify that your project uses Uglify and that you want it to run uglify on a particular directory at build time. More about Grunt in a moment.
###### bower.json
The component.json file is used to inform the Bower package manager of your projects dependencies as well as other metadata.
###### .bowerrc
The .bowerrc file is used to pass general config options to bower.
###### Karma files
Karma is a testing framework. We’ll use it to run a few tests for our Angular app. All karma files are stored in test/ folder.
I agree, there's a lot of  folders and configuration files –  but once you get more familiar with the output of a yeoman generator however, you’ll realize that all this frameworks handles most of the work for you.

Everything, what can be automazied - need to be automized, because it will ruin you and you will waste your expensive time.

Let’s add a few more things to our project before we start get on to some actual development.

###### app folder

Let's investigrate out app/ directory.

* scripts/
* styles/
* views/
* favicon.ico
* index.html
* robots.txt
* index.html

This should be familiar to most of you, this is the core html page of your app.
scripts/ directory
Again, familiar territory to most. This is where your apps javascript is stored. Note that libraries such as AngularJS will exist in the components directory, so scripts/ is for your files that you write!
styles/ directory
All your css/sass to make your app look moar pretty.
views
This nifty folder is where your Angular Templates will reside.

###### Angular JS

So, let's take a close look at angular.js architecture:

I worked with backbone.js - similar framework, and there was concept of controller(route), view(as view class) and template.

Here I can see, that we have concept of module, controller and view

The Module: **/app/scripts/app.js**

<script src="https://gist.github.com/johnyUA/535a65510a0d4e135f7d.js"></script>



The Controller: **/app/scripts/controllers/main.js**

<script src="https://gist.github.com/johnyUA/fc76a989209aab170d79.js"></script>

 

The View: app/views/main.html

<script src="https://gist.github.com/johnyUA/58dfaf31561e61399152.js"></script>

**Start the server**

It seems that grunt has it's own embedded server, that runs on 9000 port

to start server, execute

**grunt serve**

Then you should get following page

![](assets/images/Screen-Shot-2015-01-02-at-3-54-15-PM-1024x516.png)

 

#### Running tests

As I mentioned before, you can run tests

grunt test

you should get

![](assets/images/Screen-Shot-2015-01-02-at-3-58-43-PM-1024x640.png)

Test for controller looks like this

<script src="https://gist.github.com/johnyUA/cde564974557c808a749.js"></script>

 

#### Conclusion

As far, as I can make some conclusion, I can say, that this framework looks pretty good. I hope, I will write some article about continuous integration of such applications.
