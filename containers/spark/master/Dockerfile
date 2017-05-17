# nrt17.azurecr.io/nrt17/sparkmaster
#
# VERSION   0.3

FROM nrt17.azurecr.io/nrt17/sparkbase:0.3

WORKDIR /workdir

ADD code/*.* code/
ADD code/project/* code/project/

ADD code/*.sh ./
RUN chmod a+x *.sh

WORKDIR /workdir/code
RUN ls -alsR
RUN sbt clean assembly

WORKDIR /workdir

CMD [ "/bin/bash" ]
