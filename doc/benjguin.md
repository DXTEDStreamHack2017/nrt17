# Benjamin's log, commands I used and so on

## URLs, endpoints, ...

- [Flink Web UI](http://nrt17vm.westus.cloudapp.azure.com:34010)

### Kafka and Zookeeper endpoints

- kafka node 1 port 9092 is available thru nrt17vm.westus.cloudapp.azure.com:34001
- kafka node 2 port 9092 is available thru nrt17vm.westus.cloudapp.azure.com:34002
- kafka node 3 port 9092 is available thru nrt17vm.westus.cloudapp.azure.com:34003
- zookeeper node 1 port 2181 is available thru nrt17vm.westus.cloudapp.azure.com:34050

## portal az scripting

```
az network nsg rule create --resource-group NRT17 --nsg-name nrt17vm-nsg -n kafka1 --priority 1030 --destination-port-range 34001
az network nsg rule create --resource-group NRT17 --nsg-name nrt17vm-nsg -n kafka2 --priority 1040 --destination-port-range 34002
az network nsg rule create --resource-group NRT17 --nsg-name nrt17vm-nsg -n kafka3 --priority 1050 --destination-port-range 34003
```


## on my Windows laptop in Bash susbsystem

My Azure Linux VM is u2.3-4.xyz

```
rsync -ave ssh containers u2.3-4.xyz:~/sdc1/nrt17/

ssh nrt17@nrt17vm.westus.cloudapp.azure.com
```


## on my linux VM (Ubunutu 16.04 LTS)

```
export NRT17_HOME=$HOME/sdc1/nrt17
export NRT17_DOCKER_REGISTRY=nrt17.azurecr.io
export HOSTIP=`hostname -i`

docker login $NRT17_DOCKER_REGISTRY

cd $NRT17_HOME/containers
./buildimages.sh | tee ~/lastbuild.log

docker-compose up -d


docker images | grep nrt17
docker images | grep nrt17 | awk '{print $3}' | xargs --no-run-if-empty docker rmi

```

## on nrt17vm

```
sudo apt-get install     apt-transport-https     ca-certificates     curl     software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) \
  stable"
sudo apt-get update
apt-cache madison docker-ce
sudo apt-get install docker-ce=17.03.1~ce-0~ubuntu-xenial
sudo usermod -aG docker $USER
sudo systemctl enable docker
sudo chkconfig docker on

sudo apt install docker-compose

```

in .bashrc:
```
export NRT17_HOME=$HOME/sdc1/nrt17
export NRT17_DOCKER_REGISTRY=nrt17.azurecr.io
export HOSTIP=`hostname -i`
```


## saved code

```yml
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
