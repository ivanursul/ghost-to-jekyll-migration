---
layout: "post"
title:  "Always add a clean phase in your build execution"
date: 2015-09-03 11:32:19
permalink: always-add-a-clean-phase-in-your-build-execution
---


Today I faced an issue on Jenkins. I needed to fix jenkins job, which is getting info about sonar metrics, and received an error in job console
![](assets/images/Screen-Shot-2015-09-03-at-11-20-46-AM.png)

It was weird for me, because I was getting success jobs, everything went right, but as soon as I failed one job due to bad configuration - hell started, and I couldn't understand why there my job is failing all the time.So I began experiments.

After several tries I managed to add prefix **clean** phase and everything worked.
Another problem was that I was building this job with different parameters and depending on specific parameter I was receiving specific console output.But then I understood that the problem was that I had constant artifacts, that were not cleaned. And I was receiving different output because this parameter was a version of plugin , so that's why I received different output.