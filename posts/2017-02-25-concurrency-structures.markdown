---
layout: "post"
title:  "Concurrency structures written using core java"
date: 2017-02-25 09:19:37
permalink: concurrency-structures
---


### <a href="#motivation" name="motivation"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Motivation
The reason for writing this article is a lack of my understanding of Java concurrency. That's why I decided to create a post with some structures, which I wrote by myself. For sure, you may agree or disagree with me, I encourage to post your thoughts in comments so we can discuss them there.

### <a href="#list" name="list"><i class="fa fa-link anchor" aria-hidden="true"></i></a> List of structures

Implemented: 

* [Publish-Subscribe](#pubsub)
* [Queue](#queue)

Will implement them later:

* Fixed Thread Pool
* Cached Thread Pool
* CountDownLatch
* CyclicBarrier
* Phaser
* Semaphore
* Exchanger
* Rate Limiter
* Lock, ReentrantLock, ReentrantReadWriteLock
* ConcurrentHashMap
* AtomicInteger(For fun)

### <a href="#pubsub" name="pubsub"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Publish/Subscribe

What is Publish-Subscribe, in a nutshell? Obviously, there someone, who has something share something to others, and want to be sure, that everyone will receive this message.

The idea of following structure is to have a publish-subscribe mechanism, which will act asynchronously, without usage of high-level classes from `java.util.concurrent` package:

* Consumer - entity, which will consume messages from main thread.
* PubSubModel - main thread, which will send messages to the consumers. Formally, you can treat it as a consumer.

Code can be found [here](https://gist.github.com/ivanursul/2f64c8d3b81eeff348dbdd85c9823027).

Let's go over this code and try all the details:

* First of all, there will be one main thread, which will read lines from console and act as a producer, so there's no `producer` class, just a main method.
* Consumer extends **Thread** class
* Consumer runs in a while loop.
* Each consumer has it's own queue. Don't ask why, there's no serious reason for that, it was done just for fun. 
* Each queue in consumer waits to be notified.
* Notification is done in `notify(String message)` method:
* Some of you may ask why we need to wait for queue to be notified here. I filed a special question on [StackOverflow](http://stackoverflow.com/questions/42417636/what-is-the-relationship-between-thread-sleep-and-happens-before),you are free to read it, the short answer is that if we don't wait for something, then our thread will always be in **RUNNING** mode, which is not good, for whatever reasons. With example below, all consumer threads will run in **WAIT** mode most of the time.


### <a href="#queue" name="queue"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Queue

Queue is something different from publish-subscribe way of sending messages - there's a shared resource, queue, and each consumer attempts to read from it, and because of concurrency monitor, only one will manage to do that.

Code can be found [here](https://gist.github.com/ivanursul/7a7c1c0b329b70bf61719a6213f7fa37)

* All the consumers and producer have one shared resource - queue
* When producer sends message, he enters queue monitor, adds element to the queue and **notify**.
* QueueConsumer is extended by Thread and running an infinite loop.
* Inside infinite loop, consumer waits for **queue**.
* As soon as producer will execute `queue.notify()`, only one QueueConsumer will manage to enter synchronized block and read this message.
* Instead of `queue.notify()`, try `queue.notifyAll()`, the order will be removed, and there no guarantee that some threads will consume it, at all. In case of **notify**, I noticed an order in consumers: `
 message 0 
 Consumer0: Consuming message: message 0
 message 1
 Consumer1: Consuming message: message 1
 message 2
 Consumer2: Consuming message: message 2
 message 3
 Consumer0: Consuming message: message 3`

What, if we would like to decide, who is going to consume this message? Let's say, we have consumer groups and want to address some messages to some consumer groups? 
[Here is](https://gist.github.com/ivanursul/ca826fce48baed04ade754dfde102859) a new version of QueueModel.

The output will look like this:

```
Message 1
group2
group2: Consumer4: Consuming message: Message{message='Message 1', consumerGroup='group2'}
Message 2
group2
group1: Consumer5: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group1: Consumer6: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group1: Consumer7: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group1: Consumer8: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group1: Consumer9: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group1: Consumer0: Skipping message, wrong group, message: Message{message='Message 2', consumerGroup='group2'}
group2: Consumer1: Consuming message: Message{message='Message 2', consumerGroup='group2'}

```

This approach is doing his work, however, there's a problem: I don't like the idea of receiving a message if you are initially a wrong recipient. It's the same if you will get a call from post service with a package for your neighbor. That's why here is a new version of [QueueModel](https://gist.github.com/ivanursul/9cb82cf4c01fb10a74456258dea9d31f). The difference is straighforward - now each consumer group has it's own queue, so you don't need to mix things up. 