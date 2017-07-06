---
layout: "post"
title:  "Setting up ghost blogging platform"
date: 2015-02-07 16:55:04
permalink: setting-up-ghost-blogging-platform
---


Hi all!
Today I started to use ghost blogging platform.One I can say for sure - It's much more faster than wordpress or other cms.
In this article I will try to explain how to setup your application with some custom theme.
What Will we need for that ?

* Digital Ocean instance. I use Linux for all maintenance, so, if u are windows - related -> Sorry :)

* Ghost Blogging platform

* Custom theme, that you want to setup. I was lucky to buy this theme - [Pepe - Multipurpose Responsive Theme](http://themeforest.net/item/pepe-multipurpose-responsive-theme/9634807?WT.oss_phrase=&WT.oss_rank=10&WT.z_author=muvolab&WT.ac=search_list)

## Let's start

Firstly, create instance on Digital Ocean - I noticed, that ghost is much more faster, than Wordpress, so it should be enough to buy **5$** instance for first time.
Type your instance name, desired size. I recommend access by ssh, which is very powerful way for serving all your instances.

If you don't have any ssh key yet - follow [this](https://help.github.com/articles/generating-ssh-keys/) instruction on how to create one.

Once you are ready, attach your public key to digitalocean profile and select it when you will create instance. You can read [this](https://www.digitalocean.com/community/tutorials/how-to-connect-to-your-droplet-with-ssh) article for better understanding.

![Creating Digital Ocean instance](/content/images/2015/02/Screen-Shot-2015-02-07-at-6-29-42-PM.png)
![](assets/images/Screen-Shot-2015-02-07-at-6-29-58-PM.png)

It should take some time to create instance. Once it ended, go to your local computer **terminal** and type next line

> nano ~/.ssh/config

And append this lines

> 	Host blog
> 		User blogger
> 		HostName IP FROM YOUR DROPLET
> 		IdentityFile ~/.ssh/id_rsa_digitalocean

I set linux user **blogger**, but he is not create yet, so we need to ssh to your server with **root** user.

> ssh root@blog

Once you are connected, type

> adduser blogger

Type all required information, password, name, room number and other info.

Don't forget to connect user to sudo group
> sudo usermod -a -G sudo blogger

Once you are connected, exit from ssh.

Then try to connect with default user

> ssh blog

You will be connected to blogger linux user by default.

Then do some simple instuctions

> curl -sL https://deb.nodesource.com/setup | sudo bash -

> sudo apt-get install -y nodejs

> mkdir tools

> cd tools

> curl -L https://ghost.org/zip/ghost-latest.zip -o ghost.zip

> unzip -uo ghost.zip -d ghost

> cd ghost

> npm install --production

> nano config.js

edit your production host with your required url.

Then type

> npm start --production


But there is one problem. When you will release your ssh connection, your server will fall down.

so type **Control + C** and type

> screen -R ghost

If you don't have any screen, then install it.

> sudo apt-get install screen

That will create separate screen( I call it thread, like in Java language) for your ghost server.

Press enter, and type

> npm start --production

Wait until server will log that everything deploys succesfully and press **CTRL + A + D** , that combination will switch you into your main screen.
In future, if you will need to list all your screen, just type

> screen -list

Now you are ready to custmize your theme.
I am happy, that I choosed ghost for my blogging.I hope, this article will help you to start your blog rapidly.

That's all that you need to start

Some moment, on which I want to emphasize

If you will buy custom theme, and choose one click install ghost platform with digitalocean -> 90 % that you will have problems. The reason is that digitalocean instance ships with old ghost version, so there is always some troubles.


![](assets/images/Screen-Shot-2015-02-07-at-5-30-23-PM.png)


