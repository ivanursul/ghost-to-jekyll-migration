---
layout: "post"
title:  "Better application deployment with DigitalOcean, Terraform, Ansible and Docker. DNS Records"
date: 2016-10-27 21:57:59
permalink: better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records
tags: digitalocean,terraform
---


As we are creating our instances on the fly, using Terraform declarative scripts, we can also need to create dns domains and records. Let's say, you have two environments: **dev** and **prod**, and you you want to have your backend application stored under **dev.api.myapp.com** and **prod.api.myapp.com**. Luckily, terraform allows to do such things 

### Required steps

* Digitalocean account
* Ready and bought domain somewhere
* Already defined terraform instances. See article from [previous](https://ivanursul.com/better-application-deployment-with-terraform-ansible-and-docker-part-1/) part.

### <a href="#digitaloceandomain" name="digitaloceandomain"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Digital Ocean Domain

Add additional declaration to your terraform file:


```
resource "digitalocean_domain" "default" {
   name = "dev.api.myapp.com"
   ip_address = "${digitalocean_droplet.app-rest-api.ipv4_address}"
}
```

### <a href="#digitaloceanrecord" name="digitaloceanrecord"><i class="fa fa-link anchor" aria-hidden="true"></i></a>  DigitalOcean Record

If you want your app to be accessible through **dev.api.myapp.com**, without **www**m you should add this lines:

```
resource "digitalocean_record" "CNAME-www" {
  domain = "${digitalocean_domain.default.name}"
  type = "CNAME"
  name = "www"
  value = "@"
}
```

### <a href="#finalcheck" name="finalcheck"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Final Check

You should see your domain name in the list of networking domains here - [https://cloud.digitalocean.com/networking/domains](https://cloud.digitalocean.com/networking/domains)

On the [next part](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-connecting-terraform-with-ansible) we will see how to start working with cloud instances using ansible, and how to connect ansible with terraform.



