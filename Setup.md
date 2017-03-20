# Installation and setup

## Introduction

[Apache Spark](http://spark.apache.org/) is a Scala library for analysing "big data". It can be used for analysing huge (internet-scale) datasets distributed across large clusters of machines. The analysis can be anything from the computation of simple descriptive statistics associated with the datasets, through to rather sophisticated machine learning pipelines involving data pre-processing, transformation, nonlinear model fitting and regularisation parameter tuning (via methods such as cross-validation). A relatively impartial overview can be found in the [Apache Spark Wikipedia page](https://en.wikipedia.org/wiki/Apache_Spark).

Although Spark is really aimed at data that can't easily be analysed on a laptop, it is actually very easy to install and use (in [standalone mode](http://spark.apache.org/docs/latest/spark-standalone.html)) on a laptop, and a good laptop with a fast multicore processor and plenty of RAM is fine for datasets up to a few gigabytes in size. This session will walk through getting started with Spark, installing it locally (not requiring admin/root access) doing some simple counting exercises, and then looking at the analysis of some data from Twitter. After this session it should be relatively easy to take things further by reading the [Spark documentation](http://spark.apache.org/docs/latest/), which is generally pretty good.

Anyone who is interested in learning more about setting up and using Spark clusters may want to have a quick look at my [personal blog](https://darrenjw2.wordpress.com/) (mainly concerned with the Raspberry Pi), where I have previously considered [installing Spark on a Raspberry Pi 2](https://darrenjw2.wordpress.com/2015/04/17/installing-apache-spark-on-a-raspberry-pi-2/), [setting up a small Spark cluster](https://darrenjw2.wordpress.com/2015/04/18/setting-up-a-standalone-apache-spark-cluster-of-raspberry-pi-2/), and [setting up a larger Spark cluster](https://darrenjw2.wordpress.com/2015/09/07/raspberry-pi-2-cluster-with-nat-routing/). Although these posts are based around the Raspberry Pi, most of the material there is quite generic, since the Raspberry Pi is just a small (Debian-based) Linux server. On my [professional blog](https://darrenjw.wordpress.com/) I recently did a post on [a quick intro to Apache Spark](https://darrenjw.wordpress.com/2017/02/08/a-quick-introduction-to-apache-spark-for-statisticians/), and I've borrowed some material from there. I'll also be running a [short course](https://github.com/darrenjw/scala-course/blob/master/README.md) on Scala and Spark in May, and there's been some sharing of materials with that, too.

## Getting started - installing Spark

The only pre-requisite for installing Spark is a recent Java installation. On Debian-based Linux systems (such as Ubuntu), Java can be installed with:
```bash
sudo apt-get update
sudo apt-get install openjdk-8-jdk
```
For other systems you should Google for the best way to install Java. If you aren't sure whether you have Java or not, type `java -version` into a terminal window. If you get a version number of the form 1.7.x or 1.8.x you should be fine.

Once you have Java installed, you can download and install Spark in any appropriate place in your file-system. If you are running Linux, or a Unix-alike, just `cd` to an appropriate place and enter the following commands:
```bash
wget http://www.eu.apache.org/dist/spark/spark-2.1.0/spark-2.1.0-bin-hadoop2.7.tgz
tar xvfz spark-2.1.0-bin-hadoop2.7.tgz 
cd spark-2.1.0-bin-hadoop2.7
bin/run-example SparkPi 10
```
If all goes well, the last command should run an example. Don't worry if there are lots of INFO and WARN messages - we will sort that out shortly. On other systems it should simply be a matter of downloading and unpacking Spark somewhere appropriate, then running the example from the top-level Spark directory. Get Spark from the [downloads page](http://spark.apache.org/downloads.html). You should get version 2.1.0 built for Hadoop 2.7. It doesn't matter if you don't have Hadoop installed - it is not required for single-machine use.

The INFO messages are useful for debugging cluster installations, but are too verbose for general use. On a Linux system you can turn down the verbosity with:
```bash
sed 's/rootCategory=INFO/rootCategory=WARN/g' < conf/log4j.properties.template > conf/log4j.properties
```
On other systems, copy the file `log4j.properties.template` in the `conf` sub-directory to `log4j.properties` and edit the file, replacing `INFO` with `WARN` on the relevant line. Check it has worked by re-running the `SparkPi` example - it should be much less verbose this time. You can also try some other examples:
```bash
bin/run-example SparkLR
ls examples/src/main/scala/org/apache/spark/examples/
```
There are several different ways to use Spark. For this walk-through we are just going to use it interactively from the "Spark shell". We can pop up a shell with:
```bash
bin/spark-shell --master local[4]
```
The "4" refers to the number of worker threads to use. Four is probably fine for most decent laptops. `Ctrl-D` or `:quit` will exit the Spark shell and take you back to your OS shell. It is more convenient to have the Spark `bin` directory in your path. If you are using `bash` or a similar OS shell, you can temporarily add the Spark `bin` to your path with the OS shell command:
```bash
export PATH=$PATH:`pwd`/bin
```
You can make this permanent by adding a line like this (but with the full path hard-coded) to your `.profile` or similar start-up dot-file. I prefer not to do this, as I typically have several different Spark versions on my laptop and want to be able to select exactly the version I need. If you are not running `bash`, Google how to add a directory to your path. Check the path update has worked by starting up a shell with:
```bash
spark-shell --master local[4]
```
Note that if you want to run a script containing Spark commands to be run in "batch mode", you could do it with a command like:
```bash
spark-shell --driver-memory 25g --master local[4] < spark-script.scala | tee script-out.txt
```
However, it is generally better to just load the script into a running Spark shell with
```scala
:load spark-script.scala
```
There are much better ways to develop and submit batch jobs to Spark clusters, but I won't discuss those in this here. Note that while Spark is running, diagnostic information about the "cluster" can be obtained by pointing a web browser at port 4040 on the master, which here is just http://localhost:4040/ - this is extremely useful for debugging purposes.


