---
layout: "post"
title:  "Encoding Filter Not Encoding at all ?"
date: 2015-02-07 18:43:14
permalink: encoding-filter-not-encoding-at-all
---


Recently I experienced a problem, when I had CharacterEncodingFilter filter in my web.xml, but no encoding were done at all. I started to investigate this problem and find out, that few weeks ago I added additional filters on top of my web.xml, and , as a result, my CharacterEncodingFilter was placed on second or third position.Good news for me, I added integration test with ukrainian words, and my web service returned some incorrect characters.

After few minutes, I discovered, that If you want to have correct unicode data, then you need to place encoding filter at the top of all your filters. Thereafter, your filter will the first chain and you will be sure, that all other filters will work with correct unicode.

 