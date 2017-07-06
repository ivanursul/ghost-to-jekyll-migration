---
layout: "post"
title:  "Сyrillic query params in Tomcat Application Server"
date: 2015-04-01 16:08:00
permalink: syrillic-query-params-in-tomcat-application-server
tags: java,programming
---


Today I had a problem with Tomcat encoding with query params.I have solution for search entities.It was working with **latin** words. But when I tried to add query params in **cyrillic**, tomcat treat them as unreadable symbols, despite the fact, that I have URLEncodingFilter in my web application, that encodes everything in **UTF-8**,

So, request like this turns to be invalid.
    
    http://{HOST}:8080/is-lnu-rest-api/api/specoffers/types?name=Молодший
    
So, as usual, I started investigation of this issue. First, I scanned my project, especially **web.xml** deployment descriptor for some unusual encoding thing, but I failed, I found nothing. So I delegated all the investigation to google, and found that I should edit my 

	tomcat/conf/server.xml
    

You need to find Connector with port="8080" and add two lines.

    <Connector port="8080" protocol="HTTP/1.1"
    	connectionTimeout="20000"
	    useBodyEncodingForURI="true" <!-- This line -->
	    URIEncoding="UTF-8" <!-- This line -->
        redirectPort="8443" />

Restart your tomcat, and now everythig will be okay