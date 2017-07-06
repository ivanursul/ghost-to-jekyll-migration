---
layout: "post"
title:  "Java - MetaSpace vs PermGen"
date: 2015-05-05 14:10:25
permalink: java-perm-gen-vs-metaspace
---


It's been a long period since Java 8 was introduced.New Java comes with a lot of new features.

One of these features is the complete removal of the Permanent Generation (PermGen) space which has been announced by Oracle since the release of JDK 7. Interned strings, for example, have already been removed from the PermGen space since JDK 7. The JDK 8 release finalizes its decommissioning. 

This article will share the information that I found so far on the PermGen successor: Metaspace. 

The final specifications, tuning flags and documentation around Metaspace should be available on Java 8 official documentation.

#### Metaspace

The JDK 8 HotSpot JVM is now using native memory for the representation of class metadata and is called Metaspace; similar to the Oracle JRockit and IBM JVM's.

The good news is that it means no more **java.lang.OutOfMemoryError: PermGen** space problems and no need for you to tune and monitor this memory space anymore…not so fast. Instead of **java.lang.OutOfMemoryError: PermGen** you are now able to receive **java.lang.OutOfMemoryError: Metadata space** .While this change is invisible by default, we will show you next that you will still need to worry about the class metadata memory footprint. Please also keep in mind that this new feature does not magically eliminate class and classloader memory leaks. You will need to track down these problems using a different approach and by learning the new naming convention.

######PermGen space situation

This memory space is completely removed.
The PermSize and MaxPermSize JVM arguments are ignored and a warning is issued if present at start-up.

######Metaspace memory allocation model

Most allocations for the class metadata are now allocated out of native memory.
The klasses that were used to describe class metadata have been removed.

######Metaspace capacity

By default class metadata allocation is limited by the amount of available native memory (capacity will of course depend if you use a 32-bit JVM vs. 64-bit along with OS virtual memory availability).
A new flag is available (MaxMetaspaceSize), allowing you to limit the amount of native memory used for class metadata. If you don’t specify this flag, the Metaspace will dynamically re-size depending of the application demand at runtime.

######Metaspace garbage collection

Garbage collection of the dead classes and classloaders is triggered once the class metadata usage reaches the “MaxMetaspaceSize”.
Proper monitoring & tuning of the Metaspace will obviously be required in order to limit the frequency or delay of such garbage collections. Excessive Metaspace garbage collections may be a symptom of classes, classloaders memory leak or inadequate sizing for your application.

######Java heap space impact

Some miscellaneous data has been moved to the Java heap space. This means you may observe an increase of the Java heap space following a future JDK 8 upgrade.

Metaspace monitoring

Metaspace usage is available from the HotSpot 1.8 verbose GC log output.
Jstat & JVisualVM have not been updated at this point based on our testing with b75 and the old PermGen space references are still present.

######Conclusions

* Permanent Generation memory is no longer available in Java 8
* If you will try to add **-XX:MaxPermSize** or **-XX:PermSize** parameters, you will receive warning.
* Be default, Metaspace will automatically increase allocated memory, that's the main practical difference betweeb **PermGen** and **MetaSpace**.
* You can set Maxmetaspace - **-XX:MaxMetaspaceSize=128m**
* If you will reach max meta space size - Java will fail with exception, similar to PermGen exception.
