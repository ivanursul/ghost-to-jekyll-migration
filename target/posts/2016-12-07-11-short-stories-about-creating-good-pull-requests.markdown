---
layout: "post"
title:  "11 short stories about creating good pull requests"
date: 2016-12-07 17:19:44
permalink: 11-short-stories-about-creating-good-pull-requests
tags: pull requests
---


Once upon a time there was a good developer. He produced a good code, had good relations with his teammates and never break the master branch. This guy followed 11 rules and lived a long - long life. So I'm posting them here, in case someone will have a good reason to improve his way of working on pull requests.

### <a href="#makesmall" name="makesmall"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Make them small

If you plan to change your source code, don't make significant changes with dozens of classes involved. Make a granular, little changes containing < 10 files. It's important because your teammates will do a review, and in a case of big pull requests it'll take time to understand what you wrote, why you wrote it, and find possible bugs. By making smaller pull requests, you let your teammates review your code more precisely and find possible mistakes there.

### <a href="#initialpoint" name="initialpoint"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Give an initial point

If you failed with small pull requests or not, always help your reviewers with a starting point. Where should they start? What unit of logic should they review first? It won't be a problem for you to give this information and your teammates will review your PR's faster.

### <a href="#coupleofcomments" name="coupleofcomments"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Write couple of comments 

If needed, support your pull requests with a couple of comments. It may be a bad idea to put them in your code(it should be self-descriptive), but it's ok to explain some part of logic straightly inside your PR.

### <a href="#doonething" name="doonething"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Do only one thing

If you want to fix some bug, then don't start refactoring. Why on earth you need to do it within one pull request? Create two separate PR's, and this will remove all questions.

### <a href="#sync" name="sync"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Synchronize formatting configuration with your team

Most probably, all your teammates use the same IDE, so spend some time on generating default configuration for it. Time spent on this will reduce the amount of code you will send to PR's since all imports/formatting will always be the same.

### <a href="#bethefirstreviewer" name="bethefirstreviewer"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Be the first reviewer

You don't need to wait for someone to review your code - start reviewing your request as soon as you create it. Most probably, you will find some little problems right after.

### <a href="#dontbeinarush" name="dontbeinarush"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Don't be in a rush

There's nothing worse than creating pull requests, and then another right after, which contradicts with each other because you made some mistake. Don't be in a rush, test your pull request well and create just one PR.

### <a href="#description" name="description"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Good description

Do you use issue tracking system? Come on, give some title upon pull request. Write a couple of sentences for description section. Reviewer should already know what's it about before starting to review it.

### <a href="#proofs" name="proofs"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Prove your code is working

Do you use tests in your project? Cover your code with tests and write a bunch of them. You don't have tests, but you're working on some issue? Give a screenshots/logs/other evidence which signalizes that you fix it.

### <a href="#useprbuilder" name="useprbuilder"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Build your code

Use Pull Request builders. Before starting to read your code, the reviewer should already know it's working. What's the sense of reviewing it, if it doesn't work?

### <a href="#dontmerge" name="dontmerge"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Don't merge your PR if master is broken

It should be a rule of thumb - you don't merge your pull request if a master gets down. If you do this - you will get a confusion.

Remember your tests aren't a silver bullet. Your tests can suck, and your proofs from PR can be incorrect. That's why, as soon as you notice problems with master - don't touch anything!





