---
layout: "post"
title:  "Spring Security: Avoiding Basic Authentication window in your browser"
date: 2015-07-20 07:40:36
permalink: spring-security-avoiding-basic-authentication-window-in-your-browser
---


While tunnning your Spring application with Basic Authentication security you can notice that you are receiving Basic Aithentication window in your browser.
![](assets/images/servlet-basic-authentication-xml-secured-popup.png)

The problem is with [BasicAuthenticationEntryPoint](http://docs.spring.io/spring-security/site/docs/3.1.x/apidocs/org/springframework/security/web/authentication/www/BasicAuthenticationEntryPoint.html) that sends header 
	`WWW-Authenticate: Basic realm="nmrs_m7VKmomQ2YM3:"`

So, if you don't want to receive this window in your browser, just create Custom Entry Point:

<script src="https://gist.github.com/ivanursul/1c475d8939f070e8cf93.js"></script>

<script src="https://gist.github.com/ivanursul/853f918fbd7ed47ea34d.js"></script>