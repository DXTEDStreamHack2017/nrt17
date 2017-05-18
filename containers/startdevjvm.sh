#!/bin/bash

if test $# -eq 0
then
    command=$0
    echo "usage: $command <sourcespath>"
    echo "<sourcespath> exemple: $NRT17_HOME/containers/spark/master/code"
    return 0
fi

sourcespath=$1

devjvmimage="nrt17.azurecr.io/nrt17/devjvm:0.1"
docker run --name devjvm -d \
    -v $NRT17_HOME/dockervolumesforcache/maven-m2:/root/.m2 \
    -v $NRT17_HOME/dockervolumesforcache/sbt-ivy2:/root/.ivy2 \
    -v $NRT17_HOME/dockervolumesforcache/sbt-sbt:/root/.sbt \
    -v $sourcespath:/usr/src/dev \
    -w /usr/src/dev $devjvmimage

echo "devjvm container can be used to edit the sources"
docker exec -ti devjvm bash
