---
layout: "post"
title:  "tx,context, aop...How to read spring xml namespace magic ?"
date: 2016-06-15 08:16:22
permalink: tx-context-aop-how-to-read-spring-xml-namespace-magic
---


For those of you who worked with spring application without spring boot, and remembers those times, when you had to make your configuration using xml - did you ever question yourself how the magic is done ? For instance, if you had to create a component scan, you had to do 
`<context:component-scan base-package="com.yourpackage.blablabla" />` how it really did this component scan ?

######spring.handlers
Answer is simple - if you really would like to know how **context** or any other spring namespace is working - always start from **spring.handlers** file. It's present in each spring jar file, which have some namespaces. The more spring dependencies you add - the more spring.handlers files you will have 

![alt](/content/images/2016/06/Screen-Shot-2016-06-15-at-8-33-29-AM.png)

In case of spring-context jar, you will have something like this

```
http\://www.springframework.org/schema/context=org.springframework.context.config.ContextNamespaceHandler
http\://www.springframework.org/schema/jee=org.springframework.ejb.config.JeeNamespaceHandler
http\://www.springframework.org/schema/lang=org.springframework.scripting.config.LangNamespaceHandler
http\://www.springframework.org/schema/task=org.springframework.scheduling.config.TaskNamespaceHandler
http\://www.springframework.org/schema/cache=org.springframework.cache.config.CacheNamespaceHandler
```

So, let's choose **component-scan** attribute.

######component-scan
component-scan is an attribute of context namespace, so, we need **ContextNamespaceHandler**

```
/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
import org.springframework.context.annotation.ComponentScanBeanDefinitionParser;

/**
 * {@link org.springframework.beans.factory.xml.NamespaceHandler}
 * for the '{@code context}' namespace.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 */
public class ContextNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
		registerBeanDefinitionParser("property-override", new PropertyOverrideBeanDefinitionParser());
		registerBeanDefinitionParser("annotation-config", new AnnotationConfigBeanDefinitionParser());
		registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
		registerBeanDefinitionParser("load-time-weaver", new LoadTimeWeaverBeanDefinitionParser());
		registerBeanDefinitionParser("spring-configured", new SpringConfiguredBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-export", new MBeanExportBeanDefinitionParser());
		registerBeanDefinitionParser("mbean-server", new MBeanServerBeanDefinitionParser());
	}

}
```

Still nothing magical, just something about **[BeanDefinitionParser](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/xml/BeanDefinitionParser.html)**

We need this line

```
registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
```

The key thing is [ComponentScanBeanDefinitionParser](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/context/annotation/ComponentScanBeanDefinitionParser.html) and his **parse** method

```
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String[] basePackages = StringUtils.tokenizeToStringArray(element.getAttribute(BASE_PACKAGE_ATTRIBUTE),
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		// Actually scan for bean definitions and register them.
		ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
		Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
		registerComponents(parserContext.getReaderContext(), beanDefinitions, element);

		return null;
	}
```

You can, of course, dig deeper, and find many spring rules and constraints, for example one about scope-proxy attribute

```
		if (element.hasAttribute(SCOPED_PROXY_ATTRIBUTE)) {
			String mode = element.getAttribute(SCOPED_PROXY_ATTRIBUTE);
			if ("targetClass".equals(mode)) {
				scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
			}
			else if ("interfaces".equals(mode)) {
				scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
			}
			else if ("no".equals(mode)) {
				scanner.setScopedProxyMode(ScopedProxyMode.NO);
			}
			else {
				throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
			}
		}
```


######Conclusion
There's a chance, that you won't need this article, because the common way now to configure your project is java config. However, next time when you will have a chance to configure some spring xml application - try it for yourself, do your own investigation.


######Links
http://stackoverflow.com/questions/11174286/spring-xml-namespaces-how-do-i-find-what-are-the-implementing-classes-behind-t