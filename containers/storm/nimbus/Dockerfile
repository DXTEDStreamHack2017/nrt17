# nrt17.azurecr.io/nrt17/stormnimbus
#
# VERSION   0.2

FROM nrt17.azurecr.io/nrt17/stormbase:0.2

MAINTAINER boontadata <contact@boontadata.io>

WORKDIR /workdir
ADD code/target/boontadata-storm1-0.1.jar .
#ADD code/*.sh ./
#RUN chmod a+x *.sh

CMD [ "/bin/bash" ]