---
layout: "post"
title:  "Tee Streaming"
date: 2016-06-29 21:09:02
permalink: tee-streaming
---


Few days ago I faced an issue with using Java InputStream in parallel.
Imagine following situation: you have [InputStream](https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html), which you need to use in parallel. First thing - you can't use it in parallel, because InputStream keeps some pointer, which store information about where stream position is. More realistic scenario is to make first call asynchronous, and leave second as it is. But again, if we are working with streams, after we read it fully, there shouldn't be anything to read again, right ? So, this article is about problem of parallel read and how to fix them.
Watch this example to understand why parallel stream read is a bad idea
<script src="https://gist.github.com/ivanursul/f4cd44a4460452a5ee8ebb6fc1eaff98.js"></script>


The output will be similar to:

```
main thread line: Number1 Number2 Number3 Number4 Number5 Number6 Number7 Number8 Number9 Number...
t1 line: Number831 Number832 Number833 Number834 Number835 Number836 Number837 Number838 Number8...
```

As we see, some of the numbers are in the first line, and some of - in the second.

###### org.ivanursul.ghost.Main thread could be executed first

<script src="https://gist.github.com/ivanursul/8b6561a0cd9c0604f80e0b861502389d.js"></script>

The result is even funnier:
```
main thread line: Number1 Number2 Number3 Number4 Numbe
t1 line: 
```

Because main thread read everything first, there was nothing to read for t1 thread.

######TeeInputStream

<script src="https://gist.github.com/ivanursul/71e53b3ed4bae44388d22ac6280b06a5.js"></script>

The idea is quite simple
https://en.wikipedia.org/wiki/Tee_(command)

![alt](https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Tee.svg/400px-Tee.svg.png)

You read from InputStream, but in parallel, you write to another source.

So in our case, we read from InputStream, but write to ByteArrayOutputStream, which will later be used to get bytes from it. In java, we have [TeeInputStream](https://commons.apache.org/proper/commons-io/javadocs/api-1.4/org/apache/commons/io/input/TeeInputStream.html), class, which is doing exactly the same thing as Linux tee command does. You should use [Apache Commons IO](https://commons.apache.org/proper/commons-io/)

```
    /**
     * Creates a TeeInputStream that proxies the given {@link InputStream}
     * and copies all read bytes to the given {@link OutputStream}. The given
     * output stream will be closed when this stream gets closed if the
     * closeBranch parameter is {@code true}.
     *
     * @param input input stream to be proxied
     * @param branch output stream that will receive a copy of all bytes read
     * @param closeBranch flag for closing also the output stream when this
     *                    stream is closed
     */
    public TeeInputStream(
            InputStream input, OutputStream branch, boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }
```