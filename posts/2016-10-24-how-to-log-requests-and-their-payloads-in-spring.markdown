---
layout: "post"
title:  "How to log requests and their payloads in Spring"
date: 2016-10-24 22:09:35
permalink: how-to-log-requests-and-their-payloads-in-spring
---


From time to time we may need to log our requests in order to get some information. Personally, I'm writing this short article, because we need to find out the reason why jackson throws 400 error status.

Luckily, it's very easy to log your requests. Spring has a class `AbstractRequestLoggingFilter`, which has three concrete classes, which you can potentially use:

* `ServletContextRequestLoggingFilter`
* ~~`Log4jNestedDiagnosticContextFilter`~~ 
* **`CommonsRequestLoggingFilter`**

The last one is the guy we need. It's pretty straightforward how to configure this class: just declare it in your context:

```
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        return loggingFilter;
    }
```

And yes, don't forget to add additional line in your `application.properties` file:
```
logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter=DEBUG
```

And voila, you have your requests being logged:
```
2016-10-24 21:50:45.520 DEBUG 83061 --- [nio-8080-exec-3] o.s.w.f.CommonsRequestLoggingFilter      : Before request [uri=/api/requests;client=0:0:0:0:0:0:0:1]
... // My logs :)

2016-10-24 21:50:45.526 DEBUG 83061 --- [nio-8080-exec-3] o.s.w.f.CommonsRequestLoggingFilter      : After request [uri=/api/requests;client=0:0:0:0:0:0:0:1;payload={
  "title": "..."}]
```
