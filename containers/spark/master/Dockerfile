# nrt17.azurecr.io/nrt17/sparkmaster
#
# VERSION   0.4

FROM nrt17.azurecr.io/nrt17/sparkbase:0.4

MAINTAINER benjguin <web@benjguin.com>

ENV SCALA_VERSION 2.11
ENV BUILD_DIR /opt/buildapp
ENV APP_DIR /opt/app

WORKDIR ${APP_DIR}

ADD code/project   ${BUILD_DIR}/project
ADD code/src       ${BUILD_DIR}/src
ADD code/build.sbt ${BUILD_DIR}/build.sbt

ADD code/*.sh ./
RUN chmod a+x *.sh

RUN cd ${BUILD_DIR} && \
	sbt assembly && \
	cp ${BUILD_DIR}/target/scala-${SCALA_VERSION}/*.jar ${APP_DIR} && \
	rm -rf ${BUILD_DIR}

CMD [ "/bin/bash" ]
