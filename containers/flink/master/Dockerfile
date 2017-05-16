# nrt17.azurecr.io/nrt17//flinkmaster
#
# VERSION   0.1

FROM $BOONTADATA_DOCKER_REGISTRY/boontadata/flinkbase:0.1

MAINTAINER boontadata <contact@boontadata.io>

WORKDIR /workdir
ADD code/target/flink1-0.1.jar .

CMD ["/opt/flink/bin/start-master.sh"]
