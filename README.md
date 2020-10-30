# TrustSim
[![Build Version](https://img.shields.io/badge/build-v1.0-brightgreen)](https://github.com/louisheery/trust-sim)
[![Build Status](https://img.shields.io/badge/build_status-published-brightgreen)](https://github.com/louisheery/trust-sim)
[![Code Coverage](https://img.shields.io/badge/code_coverage-82%25-brightgreen)](https://github.com/louisheery/trust-sim)
[![Platform](https://img.shields.io/badge/platform-linux--64%20%7C%20win--64%20%7C%20osx--64-lightgrey)](https://github.com/louisheery/trust-sim)

A Java-based Simulation Platform and Testbed for Evaluating Mathematical Models of Trust and Reputation.

![TrustSim](https://github.com/louisheery/trust-sim/raw/master/images/architecture.png)

## Codebase Architecture
![TrustSim](https://github.com/louisheery/trust-sim/raw/master/images/codebasearchitecture.png)

## Setup Guide
### Run TrustSim Application (MacOS)
#### First-time Setup
1. Install Java SE 14 (https://www.oracle.com/java/technologies/javase-downloads.html)
2. Make sure your Java Version is set to Java 14 (check that ```$ java -version``` shows Java 14.0.2).
3. Download JavaFX 14.0.2.1 @ https://gluonhq.com/products/javafx/ -> "Latest Release".
4. Copy the downloaded openjfx-sdk-14.0.2.1 folder to the ```/Library/Java/``` folder on your computer.
5. (Optional) Set a ```$ /.bash_profile``` variable of PATH_TO_FX to the location of the JavaFX lib folder.
6. Delete the jrt-fs.jar file from inside of the C:/Program Files/Java/jdk-14.0.2/lib folder

#### Every-time Setup
1. Run Application using:
```
java --module-path $PATH TO FX --add-modules javafx.controls,javafx.fxml TrustSim.jar
```
where $PATH TO FX is the path to the lib folder of the openjfx- sdk-14.0.2.1 folder = e.g. /Library/Java/openjfx-sdk-14.0.2.1/lib/

### Run TrustSim Application (Windows)
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
java --module-path "C:\Program Files\Java\jdk-14.0.2\lib" --add-modules javafx.controls,javafx.fxml -jar TrustSim.jar
```

### Setup Cassandra & Elastic Search Server (MacOS/Windows) [with Docker]
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
