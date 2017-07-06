---
layout: "post"
title:  "Multiple javax.inject.Provider implementations and their binding"
date: 2015-09-12 20:38:26
permalink: multiple-javax-inject-provider-implementations-and-their-binding
---


Hi everyone!

Recently I became a freelancer and  started to work for one big company. It's great,  I hope to continue work there.And I also started to work on project where Dropwizard stack is used.Naturally, Guice DI framework is used ther.So, today I would like to write an article about how to bind javax.inject.Provider interfaces to their implementation. The thing is that Guice treat javax.inject.Provider specially, and you cannot just to 

<script src="https://gist.github.com/ivanursul/4d0584056338f6b282db.js"></script>
    
Because Provider is a generic interface, and google care about initializing this Providers.
So, to do that - just do the following

<script src="https://gist.github.com/ivanursul/490cdbbd299b850591ee.js"></script>

There's no special magic there, you just need to specify generic from your Provider and how to name it.And then u can do something like this

<script src="https://gist.github.com/ivanursul/acf36fd94357fecf02e0.js"></script>

See you!