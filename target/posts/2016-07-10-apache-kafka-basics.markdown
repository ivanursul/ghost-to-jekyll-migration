---
layout: "post"
title:  "Apache Kafka. Basics"
date: 2016-07-10 12:33:31
permalink: apache-kafka-basics
tags: apache kafka,event-processing,streaming
---


#### Preface

Today, we live in a world, which defines no ip addresses and is dynamically changing minute by minute. As amount of services increase every day, we receive new problems.

#### Story
Just imagine, that you have a monolithic application, which you want to rewrite into microservices architecture. You start with a single service, which, let's say, maintains profile functionality. You use MongoDB as a database.
![Profile service](/content/images/2016/07/Screen-Shot-2016-07-03-at-2-55-13-PM.png)

At this step you don't have any troubles, because, it's a single one, and it doesn't interact with other world. You developed some required amount of endpoints, everything works fine. Then imagine, that your next step is to start doing another service, let's say, billing service, which uses PostgreSQL for storing transactions. Besides that, you need to know, when Profile service receives new put request and updates some profile.

![billing service](/content/images/2016/07/Screen-Shot-2016-07-03-at-3-06-25-PM.png)

So you have two options - develop an external API for your case or work with PostgreSQL db straightly. So, you chose to work with db, and your problems begin with this point: you coupled profile and billing service together. You are signing a contract, that from now on, you need to think about two databases. And this sucks. Here's why

![Microservices coupling](/content/images/2016/07/Screen-Shot-2016-07-03-at-3-09-49-PM.png)

After some time you receive a mess with all your structure, and you can't control it, because everything is coupled. You analyse your Profile service and see, that he is doing interactions with other db's just because he needs to keep consistency of their state

![Profile usage](/content/images/2016/07/Screen-Shot-2016-07-03-at-3-13-14-PM.png)

It's not the job of Profile service to keep other state consistent. That's why it should be eliminated.

#### Event Hub

You need to somehow decouple your services, right ? I see one problem - if you are using secondary database for your work, you are breaking [Database per service](http://microservices.io/patterns/data/database-per-service.html) pattern, which explains why it's bad. 

What I propose is to think about profile update, as some event. I think, that Profile service shouldn't work with other services internal structures, but instead, he should notify them by some event. And this events should be in some event bus. Every service, who wants to know when Profile is updated, should listen to this event, and when profile get's updated - event should be fired, and everyone should get notification.

![](assets/images/Screen-Shot-2016-07-03-at-3-23-12-PM.png)

So, when you are going to speak about Kafka ? Ok, what I propose is to use Kafka as an event bus - when profile get's updated - he push a message into Kafka. In this case, Profile service is a [Kafka Producer](http://kafka.apache.org/documentation.html#intro_producers). Profile service sends this message to this event bus, which in terms of Kafka is called [Kafka Topic](http://kafka.apache.org/documentation.html#intro_topics).
Messages are appended in Kafka Topic, and they don't disappear after being consumed. That's why Kafka is a commit log. Topics are stored on Kafka Server, which is sometimes called Kafka Broker. 
![](assets/images/Screen-Shot-2016-07-03-at-3-40-23-PM.png)

#### Kafka Brokers

There can be multiple Kafka Brokers. Each Kafka Server can have multiple Leader and Follower partitions. There's a Zookeper, which stores information about topics, brokers, consumers and producers.
![](assets/images/Screen-Shot-2016-07-03-at-3-44-49-PM.png)

#### Anatomy of topic.

###### Partition
Each topic consists of one or more partitions. Partitions could be either Leader or Follower. If Partitions acts as a leader, then it handles all read and writes to the given partition. If it's a follower, then it acts as a replication, and, only in case leader will fall down, some of followers will became a leader.

###### Guarantees

At a high-level Kafka gives the following guarantees:

* Messages sent by a producer to a particular topic partition will be appended in the order they are sent. That is, if a message M1 is sent by the same producer as a message M2, and M1 is sent first, then M1 will have a lower offset than M2 and appear earlier in the log.
* A consumer instance sees messages in the order they are stored in the log.
* Kafka tolerate up to N-1 server failures without losing any messages committed to the log, where N - replication factor.


#### Consumers

###### Consumer groups

Wait, if we are speaking about messaging service, then we know, that there are two ways of consuming event messages: by using queue, when a single message is delivered to a single consumer, and pub/sub, when message is broadcast to all consumers. How Kafka achieve this ? By using Consumer groups. 

If you want to have **Queue** model - you need to have all your consumers in a single consumer group. 
![](assets/images/Screen-Shot-2016-07-03-at-4-14-53-PM.png)

If you want to have **Publish/Subscribe** - you need to have all consumers in a different consumer groups.

![](assets/images/Screen-Shot-2016-07-03-at-4-17-06-PM.png)

###### Conclusion

I'll try to write more practical article soon, for now, it's enough to start understanding kafka.

#### Links

* [Kafka Documentation](https://kafka.apache.org/documentation.html)
* [Introduction to Apache Kafka by Joe Stein](https://www.youtube.com/watch?v=qc33qMUvR7c)
* [How does Apache Kafka work?](https://www.youtube.com/watch?v=EiWsPd6JDoo)