package com.microsoft.dxted;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.TopologyBuilder;

public class TopologyRunner {
    public static IRichSpout CreateKafkaSpout() {
        String[] zkHosts = {
                "nrt17vm.westus.cloudapp.azure.com:34050"
        };
        String kafkaInputTopicName = "inputtopic";
        BrokerHosts zookeeperHosts = new ZkHosts(String.join(",", zkHosts));
        String zkRoot = "/" + kafkaInputTopicName;
        String spoutId = "onlineDataStreamSpout";
        SpoutConfig spoutConfig = new SpoutConfig(zookeeperHosts, kafkaInputTopicName, zkRoot, spoutId);
        spoutConfig.startOffsetTime = System.currentTimeMillis();

        return new KafkaSpout(spoutConfig);
    }

    public static StormTopology CreateStormTopology() {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafkaSpout", CreateKafkaSpout());
        topologyBuilder.setBolt("javaBolt", new JavaBolt()).shuffleGrouping("kafkaSpout");
        return topologyBuilder.createTopology();
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.setDebug(true);
        config.setNumWorkers(2);
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("localTopology", config, CreateStormTopology());
    }
}