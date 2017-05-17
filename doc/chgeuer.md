


# Make Kafka aware of externally visible IP address

```
externalAddress=$(curl -s -H Metadata:true http://169.254.169.254/metadata/instance/network?format=json | jq -r .interface[0].ipv4.ipaddress[0].publicip)

echo "advertised.listeners=PLAINTEXT://${externalAddress}:6667" >> /opt/kafka_2.11-0.10.0.0/config/server.properties
```
