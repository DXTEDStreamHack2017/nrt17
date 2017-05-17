


# Make Kafka aware of externally visible IP address

- `advertised.listeners`: Listeners to publish to ZooKeeper for clients to use, if different than the listeners above. In IaaS environments, this may need to be different from the interface to which the broker binds. If this is not set, the value for `listeners` will be used.	([source](https://kafka.apache.org/documentation/#brokerconfigs))


```
externalAddress=$(curl -s -H Metadata:true http://169.254.169.254/metadata/instance/network?format=json | jq -r .interface[0].ipv4.ipaddress[0].publicip)

echo "advertised.listeners=PLAINTEXT://${externalAddress}:6667" >> /opt/kafka_2.11-0.10.0.0/config/server.properties
```
