---
layout: "post"
title:  "Better application deployment with DigitalOcean, Terraform,  Ansible and Docker. Creating basic terraform instances"
date: 2016-10-27 15:03:50
permalink: better-application-deployment-with-terraform-ansible-and-docker-part-1
tags: digitalocean,terraform
---


### <a href="#argumentation" name="argumentation"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Argumentation
It's not a rare examples, when people need to deploy their applications somewhere in the cloud, without having mature infrastructure, and just for testing purpose. Let's say, you're writing a backend part for mobile application, and your android developer needs to have backend right and now. Your actions ? Of course, create EC2/Droplet instance, **scp** all binaries, connect via **ssh**, run them. You can even write some bash script, which will do all the work. And what should you do, if some day you will decide to go live ? The same thing, you will order some instances in your favorite cloud, configure load balancer, add 1-5 instances and start running your application. But how to scale ? What, if you will decide to add couple servers, and balance them ? By the time you will need to do that, you will be the author of many scripts, which potentially have bugs. You should have something better in your arsenal. I spend couple of weeks to find an instrument, which will fit my needs, and want to share it here, in my blog. It's not claims to be the best solution, I'm sure, there's plenty of them, but by following series I'll explain what I've achieved.

### <a href="#whom" name="whom"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Whom this article is for ?

This is for software engineers, who wants to improve their devops processes. I don't expect, that true devops will find something useful here, because they should already have a good bundle of instruments.

### <a href="#genearlidea" name="generalidea"><i class="fa fa-link anchor" aria-hidden="true"></i></a> General idea

You will use three projects:

* [Terraform](https://www.terraform.io/) - Cloud Creation. Since we want to reduce our mouse clicks, we're going to create instances automatically. 

* [Ansible](https://www.ansible.com/) - Instance configuration. The only thing we will need is ssh key.

* [Docker](https://www.docker.com/) - App packaging and running. There's lots of different languages and frameworks around. Why don't we make an abstraction and intoroduce docker in our infrastructure ? It's not that hard.

### <a href="#terraform" name="terrafom"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Terraform

We will use Terraform to create instances in the cloud. I will use [digitalocean.com](https://digitalocean.com), it's less mature cloud provider, than [amazon.com](https://www.amazon.com), but I love it, and want to deploy my apps there. Feel free to try EC2 for yourself.


### <a href="#preparation" name="preparations"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Preparations

Here're the steps you need to do before creating cloud instances:

*  Create digitalocean access token. Go to [https://cloud.digitalocean.com/settings/api/tokens](https://cloud.digitalocean.com/settings/api/tokens) and name your token

![](assets/images/Screen-Shot-2016-10-26-at-7-04-02-PM-1.png)

* Generate ssh keypair. Here's the instruction: [https://www.digitalocean.com/community/tutorials/how-to-use-ssh-keys-with-digitalocean-droplets](https://www.digitalocean.com/community/tutorials/how-to-use-ssh-keys-with-digitalocean-droplets)
Yes, I'm sending the link to this instructions, but if you don't know what is ssh and how to work with it - please stop at this step.

* Add this ssh key to digitalocean, here's the link - [https://cloud.digitalocean.com/settings/security](https://cloud.digitalocean.com/settings/security)

* Create folder somewhere. You will store all configs/scripts there. Name it something like `terraform-ansible-docker`

* Install terraform. Since I'm an OSX user, for me it's easy to install it - `brew install terraform`

* Install ansible - `brew install ansible`


### <a href="#createtmpfiles" name="createtmpfiles"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Create template file
Now, as you did all the required steps, you can start configuring your bootstrap script. Enter your folder, and create another folder, called *dev*. Why *dev* ? Actually, you should be ready to have different environments: one for *dev*, another for *staging*, and, finally, *prod*. Furthermore, according to [ansible best practices](http://docs.ansible.com/ansible/playbooks_best_practices.html), my suggestion is one of the options, that ansible recommends. Ok then, enter *dev* folder and create a file called *variables.tf*

```
variable "token" {
  description = "Digital Ocean Access Token"
  default = "your digital ocean token"
}

variable "ssh_fingerprint" {
  description = "ssh fingerprint"
  default="Your ssh fingerprint. Get it via command line, or get it here - https://cloud.digitalocean.com/settings/security"
}

variable "pub_key" {
	description = "ssh public key"
	default = "Your ssh public key. Copy it as it is."
}

variable "pvt_key" {
  description = "ssh private key"
  default = "Your private key. Copy it as it is."
}

variable "region" {
  description = "Digital Ocean region"
  default = "ams2"
}

```

Later on, you can remove default values, and pass everything from command line, but for now, let's keep it simple, to avoid any pitfalls.


### <a href="#createmainterfile" name="createmainterfile"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Create your main terraform file

Now, here's the final step in configuring terraform - specifying what machines do you want. Lets name it as **main.tf**:

```
provider "digitalocean" {
  token = "${var.token}"
}

resource "digitalocean_droplet" "app-rest-api" {
  count = 1
  image = "ubuntu-14-04-x64"
  name = "app-rest-api"
  region = "${var.region}"
  size = "1gb"
  ssh_keys = ["${var.ssh_fingerprint}"]
}

```

You should have following folder structure:

```
.
└── dev
    ├── main.tf
    └── variables.tf

1 directory, 2 files
```

Then, enter dev folder, and run

```
terraform plan
```

This command will plan how many instances should be created, and will output it. I will show some tricks later about your instances modification. You should get similar output:

```
Refreshing Terraform state in-memory prior to plan...
The refreshed state will be used to calculate this plan, but
will not be persisted to local or remote state storage.


The Terraform execution plan has been generated and is shown below.
Resources are shown in alphabetical order for quick scanning. Green resources
will be created (or destroyed and then created if an existing resource
exists), yellow resources are being changed in-place, and red resources
will be destroyed. Cyan entries are data sources to be read.

Note: You didn't specify an "-out" parameter to save this plan, so when
"apply" is called, Terraform can't guarantee this is what will execute.

+ digitalocean_droplet.app-rest-api
    image:                "ubuntu-14-04-x64"
    ipv4_address:         "<computed>"
    ipv4_address_private: "<computed>"
    ipv6_address:         "<computed>"
    ipv6_address_private: "<computed>"
    locked:               "<computed>"
    name:                 "app-rest-api"
    region:               "ams2"
    size:                 "1gb"
    ssh_keys.#:           "1"
    ssh_keys.0:           "${ssh_fingerprint}"
    status:               "<computed>"


Plan: 1 to add, 0 to change, 0 to destroy.
```

If you're okay with this configuration, you can start creating instances.

```
terraform apply
```

```
digitalocean_droplet.app-rest-api: Creating...
  image:                "" => "ubuntu-14-04-x64"
  ipv4_address:         "" => "<computed>"
  ipv4_address_private: "" => "<computed>"
  ipv6_address:         "" => "<computed>"
  ipv6_address_private: "" => "<computed>"
  locked:               "" => "<computed>"
  name:                 "" => "app-rest-api"
  region:               "" => "ams2"
  size:                 "" => "1gb"
  ssh_keys.#:           "" => "1"
  ssh_keys.0:           "" => "${ssh_fingerprint}"
  status:               "" => "<computed>"
digitalocean_droplet.app-rest-api: Still creating... (10s elapsed)
digitalocean_droplet.app-rest-api: Still creating... (20s elapsed)
digitalocean_droplet.app-rest-api: Still creating... (30s elapsed)
digitalocean_droplet.app-rest-api: Creation complete

Apply complete! Resources: 1 added, 0 changed, 0 destroyed.

The state of your infrastructure has been saved to the path
below. This state is required to modify and destroy your
infrastructure, so keep it safe. To inspect the complete state
use the `terraform show` command.

State path: terraform.tfstate
```

Now it's ready! You can check your digitalocean instances page and make sure there's some instances with **app-rest-api** name.

You should notice another file in your dev folder, **terraform.tfstate**:
```
.
├── main.tf
├── terraform.tfstate
└── variables.tf

0 directories, 3 files
```

This file contains all the necessary information about your instances. Feel free to `cat terraform.tfstate` it, and see what it contains.

Now, change your instance from 1gb to 2gb:
```
...
size = "2gb"
...
```

And run `terraform plan` again:

```
...

~ digitalocean_droplet.app-rest-api
    size: "1gb" => "2gb"


Plan: 0 to add, 1 to change, 0 to destroy.
```

You see it ? We increased RAM of our machine, and terraform found out, that we need to change this machine.

Run `terraform apply` again and make sure your digitalocean droplet is resized.

This is the first chapter, which explains how to work with terraform. In the [next part](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/) I will show how to set dns records for your instances

### <a href="#links" name="links"><i class="fa fa-link anchor" aria-hidden="true"></i></a> Links

* [Terraform documentation](https://ivanursul.com/better-application-deployment-with-digitalocean-terraform-ansible-and-docker-dns-records/)

* [terraform.io](https://www.terraform.io/)

PS - this blog post is now a part of [Cloud Application Deployment WIKI](http://electric-cloud.com/wiki/display/releasemanagement/Cloud+Application+Deployment)