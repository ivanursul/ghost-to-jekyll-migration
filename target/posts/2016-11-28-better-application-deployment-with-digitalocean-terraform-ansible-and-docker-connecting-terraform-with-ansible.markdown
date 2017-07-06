---
layout: "post"
title:  "Better application deployment with DigitalOcean, Terraform,  Ansible and Docker. Connecting Terraform with Ansible."
date: 2016-11-28 18:10:34
permalink: better-application-deployment-with-digitalocean-terraform-ansible-and-docker-connecting-terraform-with-ansible
tags: java,spring boot,docker,ansible,terraform
---


Just creating instances in the cloud is an intermediate result. Yes, you know don't need to create them manually, but it's not a target. We need to configure them somehow, deploy logic, restart, etc. That's why this article is about describing how to work with Terraform instances - using [Ansible](https://www.ansible.com/) tool.

### <a href="#whatdoyouneed" name="whatdoyouneed"><i class="fa fa-link anchor" aria-hidden="true"></i></a> What do you need to do before starting 

* Create terraform instance - [here's](https://ivanursul.com/better-application-deployment-with-terraform-ansible-and-docker-part-1/) how
* Install ansible: `brew install ansible`

### <a href="#howansibleknows" name="howansibleknows"><i class="fa fa-link anchor" aria-hidden="true"></i></a> How Ansible knows about Terraform 

Let's take a close look how Ansible will recognize terraform instances. Normally, this is being done by specifying instances in [inventory file](http://docs.ansible.com/ansible/intro_inventory.html) - the idea is to have a file, which contains all the IP addresses, organized by the group. Let's say, you have one load balancer and three instances of an application, then you need to have following inventory file:
```
[lb]
lb.example.com

[app]
one.example.com
two.example.com
three.example.com
```

Because we are creating instances on the fly, we don't have a predefined set of IP addresses, which we're going to use. That's why we should use [dynamic inventory](http://docs.ansible.com/ansible/intro_dynamic_inventory.html). As we're using Terraform, we need some tool, which knows how to read instances from **terraform.tfstate** file and represent them to ansible. Personally, I found it useful to use [terraform-inventory](https://github.com/adammck/terraform-inventory) script, which I found on GitHub.

To start working, you need to install it:

```
brew install terraform-inventory
```

Now let's review the folder, where we are now:
```
.
├── main.tf
└── variables.tf

One directory, two files
```
You should have already read and tried previous parts, and they are about working with Terraform:

* [Better application deployment with DigitalOcean, Terraform, Ansible, and Docker. Creating primary terraform instances](https://ivanursul.com/better-application-deployment-with-terraform-ansible-and-docker-part-1/)
* [https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/)

As we are at the root of our project, we can execute the following script to get some info about our instances, but this time, this info comes from Ansible:

```
TF_STATE=terraform.tfstate ansible app-rest-api -m setup --inventory-file=/usr/local/bin/terraform-inventory         
```

I didn't find a particular document describing this command, and this is just something that works for me.  This is just an informative command, and it does nothing. To do something, you need to write a [playbook](http://docs.ansible.com/ansible/playbooks.html).

We're going to create a simple web application, which we will deploy into our **app-rest-api** server.

### <a href="#writingwebapp" name="writingwebapp"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Writing web app 
I've wrote a [simple Spring Boot application](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/tree/a5915f4a94f2f61f9f3c083745188a64a19a4ba5), which will work just fine for our example. It will run on `8080` port, and will have a single endpoint - `http://{ip}:8080/`

As you see, we will use Docker for containerisation. I've already made a public Docker hub repository. To run it, you first need to pull it out from the repository.

```
docker pull ivanursul/terraform-ansible-spring-boot-demo
```
This command should download an image from Docker Hub. It'll take some time to do it, and after then run following command:

```
docker run -d \
-h terraform-ansible-spring-boot-demo \
-p 8080:8080 \
--restart always \
ivanursul/terraform-ansible-spring-boot-demo
```

You will get a unique container id, which you can use to get logs, stop container, etc:

```
docker logs -f 12ef980b6cf1cd7962011fd0e7fd873fabc615a9cf7b874c47eedcca0b641d70 
```

Make sure container is started by finding following line
```
2016-11-18 11:01:06.115  INFO 5 --- [           main] o.i.terraform.ansible.DemoApplication    : Started DemoApplication in 5.49 seconds (JVM running for 6.148)
```

Next, you need to get the docker machine IP, so run this command: 
```
docker-machine ls
```

This should list you all your local docker machines:

```
NAME              ACTIVE   DRIVER         STATE     URL                         SWARM   DOCKER    ERRORS
default           *        virtualbox     Running   tcp://192.168.99.100:2376           v1.12.2   
```

Then just curl it

```
curl http://192.168.99.100:8080/
Hello big world!%  
```

Don't forget to stop container:

```
docker stop 12ef980b6cf1cd7962011fd0e7fd873fabc615a9cf7b874c47eedcca0b641d70
```

Ok, so that's all the manual steps you need to do to understand how it should work: you do changes, push it to Docker hub, then pull docker image, and run it. Luckily, I did this job for you, and you only need to launch terraform and Ansible scripts. The name of Docker image is [terraform-ansible-spring-boot-demo](https://hub.docker.com/r/ivanursul/terraform-ansible-spring-boot-demo/)

### <a href="#writinplaybook" name="writingplaybook"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Writing ansible playbook

There are just two steps you need to do:

* Install Docker to your machines. We'll use a [ready ansible playbook](https://github.com/angstwad/docker.ubuntu) for that. Is there a way to install it automatically? 
* Pull image from Docker Hub public repository
* Run it using Docker Ansible command:
 
First, install docker.ubuntu role
```
ansible-galaxy install angstwad.docker_ubuntu
```

Then, create `playbook-install.yml` file with the following configuration:
```
---
- name: install docker
  hosts: app-rest-api
  become: yes
  become_method: sudo
  roles:
    - { role: angstwad.docker_ubuntu, pip_version_docker_py: 1.10.6 }

``` 

Then, create `playbook-deploy.yml`:
```
---
- hosts: app-rest-api
  become: yes
  become_method: sudo
  tasks:
    - name: Starting app-rest-api docker image
      docker:
        name: app-rest-api
        image: ivanursul/terraform-ansible-spring-boot-demo
        state: reloaded
        pull: always
        ports:
          - "8080:8080"
        expose:
          - 8080

```


And finally, `playbook-bootstrap.yml`:

```
---
- include: playbook-install.yml
- include: playbook-deploy.yml

```

All terraform configs are in previous chapters, or you can see them on github page - [terraform-ansible-spring-boot-demo](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/tree/a5915f4a94f2f61f9f3c083745188a64a19a4ba5/infrastructure), just see [main.tf](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/blob/a5915f4a94f2f61f9f3c083745188a64a19a4ba5/infrastructure/main.tf) and [variables.tf](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/blob/a5915f4a94f2f61f9f3c083745188a64a19a4ba5/infrastructure/variables.tf)

### <a href="#smoketest" name="smoketest"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Smoke test

You should have your `${DIGITALOCEAN_ACCESS_TOKEN}` and `${SSH_FINGERPRINT}` variables set. Also, your ssh key should be located under `$HOME/.ssh/` folder and should have `id_rsa_do_token` name.

First, let's create instances with terraform:

```
terraform apply \  
-var "token=${DIGITALOCEAN_ACCESS_TOKEN}" \
-var "pub_key=$HOME/.ssh/id_rsa_do_token.pub" \
-var "pvt_key=$HOME/.ssh/id_rsa_do_token" \
-var "ssh_fingerprint=${SSH_FINGERPRINT}"
```

Then, let's execute ansible `playbook-bootstrap.yml` script:

```
TF_STATE=terraform.tfstate ansible-playbook --inventory-file=/usr/local/bin/terraform-inventory playbook-bootstrap.yml
```

Then it should be possible to access your service via external ip. Check it out.

### <a href="#results" name="results"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Results
The connection of Terraform and Ansible tools gave us a good way of creating instances. Also, Docker brings a significant improvement on deploying your application. You should prepare five things to start: cloud access token(Amazon, Azure, DigitalOcean), ssh key, `*.tf` for Terraform, `playbook-*.yml` and `Dockerfile`. Personally, I find this set of tools to be a very powerful and useful approach to deploying and maintaining your applications. 

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Better application deployment with DigitalOcean, Terraform, Ansible and Docker. Creating basic terraform instances. Part1](https://ivanursul.com/better-application-deployment-with-terraform-ansible-and-docker-part-1/)
* [Better application deployment with DigitalOcean, Terraform, Ansible and Docker. DNS Records. Part2](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/)
* [ terraform-ansible-spring-boot-demo github repo](https://github.com/ivanursul/terraform-ansible-spring-boot-demo/tree/a5915f4a94f2f61f9f3c083745188a64a19a4ba5)
* [ansible.com](https://www.ansible.com/)
* [terraform-inventory](https://github.com/adammck/terraform-inventory)