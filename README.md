# TrustSim
A Testbed for Evaluating Trust

## Setup Cassandra & Elastic Search Server (MacOS)
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
