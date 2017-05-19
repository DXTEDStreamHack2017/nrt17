#!/bin/bash

hdfs dfs -rm /clustervolume/*
hdfs dfs -copyFromLocal code/target/scala-2.11/nrt17-spark-job1-assembly-0.1.jar /clustervolume

spark-submit \
    --class com.benjguin.nrt17.sparkjob1.Main \
    --deploy-mode cluster \
    --master spark://sparkm1:6066 \
    /clustervolume/nrt17-spark-job1-assembly-0.1.jar \
    ks1:9092,ks2:9092,ks3:9092 sampletopic
