# nrt17.azurecr.io/nrt17/flinkmaster
#
# VERSION   0.1

FROM nrt17.azurecr.io/nrt17/flinkbase:0.1

MAINTAINER chgeuer <chgeuer@microsoft.com>

ENV SCALA_VERSION 2.11
ENV SCALA_VERSION_LONG 2.11.7
ENV HADOOP_VERSION 2.7.0
ENV HADOOP_VERSION_SHORT 27
ENV FLINK_VERSION 1.2.0
ENV FLINK_ROOT_DIR /opt/flink
ENV BUILD_DIR /opt/buildapp
ENV APP_DIR /opt/app

WORKDIR .

ADD project   ${BUILD_DIR}/project
ADD src       ${BUILD_DIR}/src
ADD build.sbt ${BUILD_DIR}/build.sbt

RUN cd ${BUILD_DIR} && \
	sbt assembly && \
	mkdir -p ${APP_DIR} && \
	cp ${BUILD_DIR}/target/scala-${SCALA_VERSION}/*.jar ${APP_DIR} && \
	rm -rf ${BUILD_DIR}

CMD ["/opt/flink/bin/start-master.sh"]
