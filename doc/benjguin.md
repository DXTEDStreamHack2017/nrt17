# Benjamin's log, commands I used and so on

## on my Windows laptop in Bash susbsystem

My Azure Linux VM is u2.3-4.xyz

```
rsync -ave ssh containers u2.3-4.xyz:~/sdc1/nrt17/
```


## on my linux VM (Ubunutu 16.04 LTS)

```
export NRT17_HOME=$HOME/sdc1/nrt17
export NRT17_DOCKER_REGISTRY=$HOME/sdc1/nrt17

docker login $NRT17_DOCKER_REGISTRY

cd $NRT17_HOME/containers
./buildimages.sh

docker-compose up -d
```

## saved code

```yml
  flink-master:
    image: $NRT17_DOCKER_REGISTRY/nrt17/flinkmaster:0.1
    ports:
      - "34010:8081"
      - "34011:6123"
    container_name: flink-master
    hostname: flink-master
    networks:
      - nrt17net
  flink-worker1:
    image: $NRT17_DOCKER_REGISTRY/nrt17/flinkworker:0.1
    ports:
      - "34012:6121"
      - "34013:6122"
    container_name: flink-worker1
    hostname: flink-worker1
    networks:
      - nrt17net
  flink-worker2:
    image: $NRT17_DOCKER_REGISTRY/nrt17/flinkworker:0.1
    ports:
      - "34014:6121"
      - "34015:6122"
    container_name: flink-worker2
    hostname: flink-worker2
    networks:
      - nrt17net
  sparkm1:
    image: $NRT17_DOCKER_REGISTRY/nrt17/sparkmaster:0.1
    container_name: sparkm1
    hostname: sparkm1
    command: spark-class org.apache.spark.deploy.master.Master -h sparkm1
    environment:
      MASTER: spark://sparkm1:7077
      SPARK_CONF_DIR: /conf
      SPARK_PUBLIC_DNS: localhost
      KAFKA_ADVERTISED_SERVERS: "ks1:9092,ks2:9092,ks3:9092"
    ports:
      - 34101:4040
      - 34102:6066
      - 34103:7001
      - 34104:7002
      - 34105:7003
      - 34106:7004
      - 34107:7005
      - 34108:7006
      - 34109:7077
      - 34110:8080
    volumes:
      - /$NRT17_HOME/dockervolumesforcache/sparkhdfs:/clustervolume
    networks:
      - nrt17net
  sparkw1:
    image: $NRT17_DOCKER_REGISTRY/nrt17/sparkworker:0.1
    container_name: sparkw1
    hostname: sparkw1
    command: spark-class org.apache.spark.deploy.worker.Worker spark://sparkm1:7077
    environment:
      SPARK_CONF_DIR: /conf
      SPARK_WORKER_CORES: 2
      SPARK_WORKER_MEMORY: 1g
      SPARK_WORKER_PORT: 8881
      SPARK_WORKER_WEBUI_PORT: 8081
      SPARK_PUBLIC_DNS: localhost
      KAFKA_ADVERTISED_SERVERS: "ks1:9092,ks2:9092,ks3:9092"
    depends_on:
      - sparkm1
    ports:
      - 34120:7012
      - 34121:7013
      - 34122:7014
      - 34123:7015
      - 34124:7016
      - 34125:8881
      - 34126:8081
    volumes:
      - /$NRT17_HOME/dockervolumesforcache/sparkhdfs:/clustervolume
    networks:
      - nrt17net
  sparkw2:
    image: $NRT17_DOCKER_REGISTRY/nrt17/sparkworker:0.1
    container_name: sparkw2
    hostname: sparkw2
    command: spark-class org.apache.spark.deploy.worker.Worker spark://sparkm1:7077
    environment:
      SPARK_CONF_DIR: /conf
      SPARK_WORKER_CORES: 2
      SPARK_WORKER_MEMORY: 1g
      SPARK_WORKER_PORT: 8881
      SPARK_WORKER_WEBUI_PORT: 8081
      SPARK_PUBLIC_DNS: localhost
      KAFKA_ADVERTISED_SERVERS: "ks1:9092,ks2:9092,ks3:9092"
    depends_on:
      - sparkm1
    ports:
      - 34130:7012
      - 34131:7013
      - 34132:7014
      - 34133:7015
      - 34134:7016
      - 34135:8881
      - 34136:8081
    volumes:
      - /$NRT17_HOME/dockervolumesforcache/sparkhdfs:/clustervolume
    networks:
      - nrt17net
  stormnimbus1:
    image: $NRT17_DOCKER_REGISTRY/nrt17/stormnimbus:0.1
    container_name: stormnimbus1
    hostname: stormnimbus1
    command: storm nimbus
    depends_on:
      - zk1
    restart: always
    ports:
      - 34201:6627
    networks:
      - nrt17net
  stormui:
    image: $NRT17_DOCKER_REGISTRY/nrt17/stormnimbus:0.1
    container_name: stormui
    hostname: stormui
    command: storm ui
    depends_on:
      - zk1
      - stormnimbus1
    ports:
      - 34200:8080
    networks:
      - nrt17net
  stormsupervisor1:
    image: $NRT17_DOCKER_REGISTRY/nrt17/stormsupervisor:0.1
    container_name: stormsupervisor1
    hostname: stormsupervisor1
    command: storm supervisor
    depends_on:
      - zk1
      - stormnimbus1
    networks:
      - nrt17net
  stormsupervisor2:
    image: $NRT17_DOCKER_REGISTRY/nrt17/stormsupervisor:0.1
    container_name: stormsupervisor2
    hostname: stormsupervisor2
    command: storm supervisor
    depends_on:
      - zk1
      - stormnimbus1
    networks:
      - nrt17net
  stormsupervisor3:
    image: $NRT17_DOCKER_REGISTRY/nrt17/stormsupervisor:0.1
    container_name: stormsupervisor3
    hostname: stormsupervisor3
    command: storm supervisor
    depends_on:
      - zk1
      - stormnimbus1
    networks:
      - nrt17net
```
