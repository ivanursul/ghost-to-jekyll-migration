---
layout: "post"
title:  "watch - useful unix command I've never heard of"
date: 2016-12-01 13:11:37
permalink: watch-useful-unix-command-ive-never-heard-of
tags: linux
---


I use ImageMagick for resizing images in one of the projects, and I needed a command to monitor folder changes in a temporary folder. So I found [watch](https://linux.die.net/man/1/watch):

```
watch -d -n 0.3 'ls -l | grep ivanursul'
```

This command check every **0.3** seconds for current folder, and find all files, created by user **ivanursul**


![](assets/images/out.gif)

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Linux Watch](https://linux.die.net/man/1/watch)