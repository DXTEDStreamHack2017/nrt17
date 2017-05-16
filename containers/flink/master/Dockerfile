# nrt17.azurecr.io/nrt17//flinkmaster
#
# VERSION   0.1

FROM $BOONTADATA_DOCKER_REGISTRY/boontadata/flinkbase:0.1

ENV HADOOP_VERSION 2.7.0
ENV FLINK_VERSION 1.1.3
ENV SCALA_VERSION 2.11
ENV FLINK_ROOT_DIR /opt/flink

MAINTAINER chgeuer <chgeuer@microsoft.com>

RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
	apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823&& && \
	apt-get update && \
	apt-get install sbt

WORKDIR .

RUN ${FLINK_ROOT_DIR}/conf/

ADD target/scala-2.11/chgeuername_2.11-01.SNAPSHOT.jar .

CMD ["/opt/flink/bin/start-master.sh"]
