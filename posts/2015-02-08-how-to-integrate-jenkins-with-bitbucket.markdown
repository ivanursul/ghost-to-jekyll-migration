---
layout: "post"
title:  "How to integrate Jenkins with Bitbucket"
date: 2015-02-08 08:24:51
permalink: how-to-integrate-jenkins-with-bitbucket
---


Recently I had an issue with integrating Jenkins CI with Bitbucket CVS.
My goal was to trigger jenkins to start running job, when commit to bitbucket will be pushed.
Steps I made to made jenkins trigger job:

###### Install Bitbucket plugin
Set the Jekins job's Build trigger to Poll SCM, but do not specify a schedule
###### Create a github post-receive trigger to notify the URL
http://yourserver/git/notifyCommit?url=<URL of the Git repository>
example

http://builds.yoursite.com/git/notifyCommit?url=git@bitbucket.org:nick/project.git
This will tell Jenkins to trigger whenever code in bitbucket will be changed.

Note, that Jenkins will check whether changes were made and will start only in case of new changes in repository.