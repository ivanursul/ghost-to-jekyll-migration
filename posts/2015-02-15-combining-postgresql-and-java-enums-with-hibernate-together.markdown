---
layout: "post"
title:  "Combining POSTGRESQL and JAVA enums with HIBERNATE together"
date: 2015-02-15 09:39:41
permalink: combining-postgresql-and-java-enums-with-hibernate-together
---


How many times did you have to use java enums in your JPA/Hubernate entities ? Yes, instead of having some relations with reference-tables, which are some sort of dictionaries, you wrote simple field with **varchar** type

Something like class **Model** with **RowStatus** enum
<script src="https://gist.github.com/johnyUA/8c05eddba45aaf60e1df.js"></script>

<script src="https://gist.github.com/johnyUA/5ee2b70b64a73699ec32.js"></script>

RowStatus is used to detemine whether row is deleted from the system, or row is active.It seems, that this should be enough to have no problems while using this functionality.

But there is one potential problem: When we set enum in our system, we made a contract, that we will send only values from **RowStatus** enum. But how about updating data outside our system ? For example, system administrator of your database made some critical update  and committed some critical bug - instead of values from RowStatus - **ACTIVE**, **DELETED** he updated row with syntax error - **DELITED**. From now on, when Hibernate will try to map such row - he will throw an exception.
That's an issue. 

#### Possible solutions ?

The only solution that I can offer you is that you will controll adding your data on database. You can use database enums, constraints, triggers, etc...

I choosed postgres enums.

To add such, you simply need to create your new type

> CREATE TYPE qrow_status AS ENUM ('ACTIVE', 'DELETED', 'REATTACHED');

Then you are free to create your tables with new type

> CREATE TABLE q_rf_streettype
> (
  
 >  id bigserial NOT NULL, 
  
 >  status **qrow_status** NOT NULL,
 
 > ...
 
> )

You can work with this column as simple **varchar** type.


#### Combine with JPA/Hibernate.

Modificate your entity

<script src="https://gist.github.com/johnyUA/e380880279088e82b988.js"></script>

and add additional class

<script src="https://gist.github.com/johnyUA/6b1de7a41202f9a01d51.js"></script>

Remember, that your enum column should now have **row_status** type, not **varchar** or some else.