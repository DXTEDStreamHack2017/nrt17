# nrt17.azurecr.io/nrt17/kafka
#
# VERSION   0.1

FROM anapsix/alpine-java

MAINTAINER Wurstmeister 

ENV KAFKA_VERSION="0.10.0.0" SCALA_VERSION="2.11"
ENV KAFKA_HOME /opt/kafka_${SCALA_VERSION}-${KAFKA_VERSION}

ADD download-kafka.sh /tmp/download-kafka.sh
ADD start-kafka.sh    /usr/bin/start-kafka.sh
ADD broker-list.sh    /usr/bin/broker-list.sh
ADD create-topics.sh  /usr/bin/create-topics.sh

RUN apk add --update unzip wget curl docker jq coreutils && \
	chmod a+x /tmp/download-kafka.sh && \
	/tmp/download-kafka.sh && \
	tar xfz /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /opt && \
	rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz && \
	chmod a+x /usr/bin/*.sh

VOLUME ["/kafka"]

# Use "exec" form so that it runs as PID 1 (useful for graceful shutdown)
CMD ["start-kafka.sh"]
