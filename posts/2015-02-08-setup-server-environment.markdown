---
layout: "post"
title:  "How to setup java enviroment on server"
date: 2015-02-08 09:50:58
permalink: setup-server-environment
---


Just to generalize all that I studied from administration field, I decided to write all steps needed to setup your java environment on the web.

#### What will we need?

Server/local machine.Say, you need to deploy your app on the web and you need a server. I will be using digitalocean for that purpose. Of course, you will need ssh access.
Java Development Kit. I will be using jdk 1.7 
Maven build tool. For building apps. We can setup as much tools, as we want(Ant, Gradle), I choosed maven because I use it in my daily development.
Setting up ssh key

If you already have ssh key , then you are free to skip this part.

Go to your terminal and type

> ssh-keygen -t rsa

You will be asked to enter ssh key location - I choosed 

> ~/.ssh/id_rsa_example

After that, executed:

> cat ~/.ssh/id_rsa_example.pub

You will get public key, that you need to store in your digitalocean instance in SSH page

Name this ssh and copy output from previous cat procedure.

![](assets/images/Screen-Shot-2015-01-01-at-4-45-55-PM-300x182.png)

PS - Adding your ssh to digitalocean menu means, that when you will create server instance and choose that ssh key in wizard - it will automatically appear in server instance ~/.ssh/authorized_keys.

PS - if you will forget to attach your ssh key to appropriate droplet you can always add this ssh key manually by typing

> ssh-copy-id user@host // In Linux
cat ~/.ssh/id_rsa_example.pub | ssh user@host "cat >> ~/.ssh/authorized_keys" // Os x

Finally, you are ready to create server instance.

#### Creating server instance

It's up to you how to create your server instance, I usually do this by Digitalocean web application.

So, go to digital ocean profile page, and find Create Droplet button

![](assets/images/Screen-Shot-2015-01-01-at-5-02-40-PM-300x191.png)

 

And second part

![](assets/images/Screen-Shot-2015-01-01-at-5-03-27-PM-300x191.png)



##### Finding IP and Creating restricted user

Go to Droplets page in your Digitalocean account and find your just created server. There should be some assigned ip for your server. Remember it.

Then execute

> ssh root@{your ip}

and create new user:

> adduser customuser

follow wizard, type password, etc..

#### SSH Configuration

Then go your local pc Terminal and type

> nano ~/.ssh/config

and add next configuration:

	Host server-example
        User customuser
        HostName {ip of server}
        IdentityFile ~/.ssh/id_rsa_example
Now you are ready to connect to your server in convenient way

You can type

> ssh server-example

and start using your server.

#### Java configuration

> ssh example-server

> mkdir tools

> wget https://www.dropbox.com/s/9ffnfp5nbhlhy82/jdk-7u71-linux-i586.tar.gz?dl=0

> tar zxvf jdk-7u71-linux-i586.tar.gz

> ln -s /home/customuser/tools/jdk1.7.0_71 /home/customuser/tools/java

> cd

> nano .bashrc

add this lines to the end of file

> export JAVA_HOME=~/tools/java

> export PATH=$PATH:$JAVA_HOME/bin

press Control + X , then type yes

#### Maven configuration

The same with maven

> ssh example-server

> wget https://www.dropbox.com/s/gywe7n81nt89cv2/apache-maven-3.2.5-bin.tar.gz?dl=0

> tar zxvf apache-maven-3.2.5-bin.tar.gz

> ln -s /home/customuser/tools/apache-maven-3.2.5 /home/customuser/tools/maven

> cd

> nano .bashrc

add additional configuration.

> export M2_HOME=~/tools/maven

> export JAVA_HOME=~/tools/java

> export PATH=$PATH:$M2_HOME/bin:$JAVA_HOME/bin

Tha's all, now you are ready to deploy your apps to remote server.

PS - I will be happy to receive some criticism from you, that would help me to improve this article.