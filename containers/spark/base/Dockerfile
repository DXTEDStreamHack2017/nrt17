# nrt17.azurecr.io/nrt17/sparkbase
#
# VERSION   0.4

FROM gettyimages/spark:2.1.0-hadoop-2.7

RUN apt-get update && \
  apt-get -y upgrade && \
  apt-get install -y curl && \
  apt-get install -y apt-transport-https && \
  echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
  apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823 && \
  apt-get update && \
  apt-get install -y sbt

WORKDIR /usr/src/dev

ENTRYPOINT ["init"]
