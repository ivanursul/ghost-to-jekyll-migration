---
layout: "post"
title:  "How to limit number of requests"
date: 2016-12-06 20:37:45
permalink: how-to-limit-number-of-requests
---


Did you ever need to limit requests coming to your endpoints? Say, your maximum design capacity is near 10k requests/second, and you don't want to probe your service on higher rates. 

I choose to use [RateLimiter](https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/RateLimiter.java) - small class from [Guava](https://github.com/google/guava).

The init process aren't so huge, just couple of lines:
```
RateLimiter rateLimiter = RateLimiter.create(10); // 10 requests/second
```

Then, just place `rateLimiter` in place, where you need to have limited requests:

```
boolean isAcquired = rateLimiter.tryAcquire();

if (!isAcquired) {
    throw new NotSoFastBuddyException(...);
}
```

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Guava](https://github.com/google/guava)