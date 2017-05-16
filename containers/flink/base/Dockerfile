# # nrt17.azurecr.io/nrt17//flinkbase
#
# VERSION   0.1

# from https://github.com/melentye/flink-docker
FROM java:openjdk-9-jdk

ENV HADOOP_VERSION 2.7.0
ENV FLINK_VERSION 1.2.0
ENV SCALA_VERSION 2.11
ENV SCALA_VERSION_LONG 2.11.7
ENV FLINK_ROOT_DIR /opt/flink

# Install Scala & SBT
RUN apt-get -y upgrade && apt-get -y update && \
    apt-get -y install gnupg apt-transport-https rsync && \
    curl --get --url http://www.scala-lang.org/files/archive/scala-${SCALA_VERSION_LONG}.deb --output scala-${SCALA_VERSION_LONG}.deb && \
    dpkg -i scala-${SCALA_VERSION_LONG}.deb && \
    rm -f scala-${SCALA_VERSION_LONG}.deb && \
    echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
    apt-get -y update && \
    ln -s $JAVA_HOME/lib $JAVA_HOME/conf && \
    apt-get install sbt && \
    apt-get clean && rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

# Get Hadoop from US Apache mirror and extract just the native
# libs. (Until we care about running HDFS with these containers, this
# is all we need.)
RUN mkdir -p /opt && \
    cd /opt && \
    curl --get --url http://www.us.apache.org/dist/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz --output hadoop-${HADOOP_VERSION}.tar.gz \
    tar -xvfz hadoop-${HADOOP_VERSION}.tar.gz -C hadoop-${HADOOP_VERSION}/lib/native && \
    ln -s hadoop-${HADOOP_VERSION} hadoop && \
    echo Hadoop ${HADOOP_VERSION} native libraries installed in /opt/hadoop/lib/native

# Get Flink from US Apache mirror.
RUN mkdir -p /opt && \
    cd /opt && \
    curl --get --url http://www.us.apache.org/dist/flink/flink-${FLINK_VERSION}/flink-${FLINK_VERSION}-bin-hadoop27-scala_${SCALA_VERSION}.tgz --output flink-${FLINK_VERSION}-bin-hadoop27-scala_${SCALA_VERSION}.tgz | \
    tar -xvfz flink-${FLINK_VERSION}-bin-hadoop27-scala_${SCALA_VERSION}.tgz && \
    ln -s flink-${FLINK_VERSION} flink && \
    echo Flink ${FLINK_VERSION} installed in /opt

ADD log4j.properties logback.xml ${FLINK_ROOT_DIR}/conf/
ADD start-common.sh start-worker.sh start-master.sh ${FLINK_ROOT_DIR}/bin/
ADD flink-conf.yaml ${FLINK_ROOT_DIR}/conf/flink-conf.yaml
ENV PATH $PATH:${FLINK_ROOT_DIR}/bin
