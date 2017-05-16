#!/bin/bash

#usage: . buildimages.sh <reset|noreset>
if test $# -lt 1; then reset=noreset; else reset=$1; fi

NRT17_DOCKER_REGISTRY=nrt17.azurecr.io

if test -z $NRT17_HOME
then
    echo NRT17 variable must not be null or empty
    return 1
fi

build_and_push()
{
    folderpath=$1
    filepath=$folderpath/Dockerfile
    filepath2=$folderpath/tmpDockerfile
    tagname=$(eval echo "`head -1 $filepath | awk '{print $2}'`")
    tagversion=`head -3 $filepath | tail -1| awk '{print $3}'`
    fulltag="$tagname:$tagversion"

    if test $reset = "reset"
    then
        echo "will reset image $fulltag"
        docker rmi $fulltag
    fi

    imageavailability=`docker images | grep "$tagname *$tagversion"`
    if test -n "$imageavailability"
    then
        echo "local image $fulltag already exists, no reset so no rebuild"
    else
        echo "will build $fulltag"
        if test -e $filepath2; then rm $filepath2; fi
        replacestring="s/\$NRT17_DOCKER_REGISTRY/${NRT17_DOCKER_REGISTRY}/g"
        sed $replacestring $filepath > $filepath2

        docker build -t $fulltag $folderpath --file $filepath2
        echo "local docker images for $tagname:"
        docker images | grep "$tagname"
        docker push $fulltag
    fi
}

build_and_push $NRT17_HOME/containers/zookeeper
build_and_push $NRT17_HOME/containers/kafka
build_and_push $NRT17_HOME/containers/flink/base
build_and_push $NRT17_HOME/containers/flink/master
build_and_push $NRT17_HOME/containers/flink/worker
build_and_push $NRT17_HOME/containers/spark/base
build_and_push $NRT17_HOME/containers/spark/master
build_and_push $NRT17_HOME/containers/spark/worker
build_and_push $NRT17_HOME/containers/storm/base
build_and_push $NRT17_HOME/containers/storm/master
build_and_push $NRT17_HOME/containers/storm/worker
build_and_push $NRT17_HOME/containers/storm/zookeeper

docker images | grep $NRT17_DOCKER_REGISTRY
