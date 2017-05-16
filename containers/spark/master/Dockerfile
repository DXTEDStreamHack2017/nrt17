# nrt17.azurecr.io/nrt17/sparkmaster
#
# VERSION   0.2

FROM nrt17.azurecr.io/nrt17/sparkbase:0.1

WORKDIR /workdir

ADD code/*.* ./




ADD code/target/scala-2.11/boontadata-spark-job1-assembly-0.1.jar ./
ADD code/*.sh ./
RUN chmod a+x *.sh

CMD [ "/bin/bash" ]
