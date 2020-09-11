# TrustSim
[![Build Version](https://img.shields.io/badge/build-v1.0-brightgreen)](https://github.com/louisheery/trust-sim)
[![Build Status](https://img.shields.io/badge/build_status-published-brightgreen)](https://github.com/louisheery/trust-sim)
[![Code Coverage](https://img.shields.io/badge/code_coverage-82%-brightgreen)](https://github.com/louisheery/trust-sim)
[![Platform](https://img.shields.io/badge/platform-linux--64%20%7C%20win--64%20%7C%20osx--64%20%7C-lightgrey)](https://github.com/louisheery/trust-sim)

A Testbed for Evaluating Trust

![TrustSim](https://github.com/louisheery/trust-sim/raw/master/images/architecture.png)

## Codebase Architecture
![TrustSim](https://github.com/louisheery/trust-sim/raw/master/images/codebasearchitecture.png)


## Run TrustSim Application (Windows)
#### First-time Setup
1. Install Java SE 14 (https://www.oracle.com/java/technologies/javase-downloads.html) (additional help @ https://java.tutorials24x7.com/blog/how-to-install-java-14-on-windows)
2. Make sure your Java Version is set to Java 14 (check that ```$ java -version``` shows Java 14.0.2).
3. Download JavaFX 14.0.2.1 @ https://gluonhq.com/products/javafx/ -> "Latest Release".
4. Copy the bin folder contents from the JavaFX download folder into the bin folder of jdk-14.0.2 folder (e.g. at C:\Program Files\Java\jdk-14.0.2\bin)
5. Copy the lib folder contents from the JavaFX download folder into the lib folder of jdk-14.0.2 folder (e.g. at C:\Program Files\Java\jdk-14.0.2\lib)
6. Delete the jrt-fs.jar file from inside of the C:/Program Files/Java/jdk-14.0.2/lib folder

#### Every-time Setup
1. Run Application using:
```
java --module-path "C:\Program Files\Java\jdk-14.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar TrustSim-1.0.jar
```

## Setup Cassandra & Elastic Search Server (MacOS) [with Docker]
#### First-time Setup
1. Install Docker Desktop: https://www.docker.com/products/docker-desktop
2. Download the JanusGraph Docker Configuration Files from:
```
git clone https://github.com/JanusGraph/janusgraph-docker.git
cd janusgraph-docker
```
3. Run the Docker Setup
This will create 3 Docker Containers (jce-janusgraph, jce-elastic, jce-cassandra) in a group called janusgraph-docker-master
```
docker-compose -f docker-compose-cql-es.yml up
```
#### Every-time Setup
```
$ docker start jce-janusgraph jce-elastic jce-cassandra // to start
$ docker stop jce-janusgraph jce-elastic jce-cassandra // to stop
```

## Setup Cassandra & Elastic Search Server (MacOS) [without Docker]
### 1. Setup Cassandra (from: https://gist.github.com/ssmereka/e41d4ad053a547611ba7ef1dac4cc826)
#### First-time Setup
```
brew install java
mkdir -p ~/opt/packages && cd $_
curl -O https://www.apache.org/dyn/closer.lua/cassandra/3.11.6/apache-cassandra-3.11.6-bin.tar.gz
gzip -dc apache-cassandra-3.7-bin.tar.gz | tar xf -
ln -s ~/opt/packages/apache-cassandra-3.7 ~/opt/cassandra
mkdir -p ~/opt/cassandra/data/data
mkdir -p ~/opt/cassandra/data/commitlog
mkdir -p ~/opt/cassandra/data/saved_caches
mkdir -p ~/opt/cassandra/logs
```
Add following to ~/.profile:
```
if [ -d "$HOME/opt" ]; then
    PATH="$PATH:$HOME/opt/cassandra/bin"
fi
```

#### Every-time Setup
```
source ~/.profile
cassandra -f
```
### 2. Setup ElasticSearch (from: https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-install.html)
#### First-time Setup
```
curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.8.0-darwin-x86_64.tar.gz
tar -xvf elasticsearch-7.8.0-darwin-x86_64.tar.gz
```

#### Every-time Setup
```
cd elasticsearch-7.8.0/bin
./elasticsearch
```
