package com.benjguin.nrt17.sparkjob1

import java.sql.Timestamp
import java.text.{DateFormat, SimpleDateFormat}

import collection.JavaConversions._
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._ 
import com.benjguin.nrt17.sparkjob1.proto.messages.TrackingPacket
import org.apache.spark.sql.Encoders

object Main {

  private val logger = Logger.getLogger(this.getClass)

  def main(args: Array[String]) {

    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)
    Logger.getLogger("kafka").setLevel(Level.WARN)

    logger.setLevel(Level.INFO)

    val sparkJob = new SparkJob()
    try {
      sparkJob.runJob()
    } catch {
      case ex: Exception =>
        logger.error(ex.getMessage)
    }
  }
}

class SparkJob extends Serializable {
  @transient lazy val logger = Logger.getLogger(this.getClass)

  logger.setLevel(Level.INFO)
  val sparkSession =
    SparkSession.builder
      .master("sparkm1")
      .appName("nrt17sparkjob1")
      .getOrCreate()
  
  def runJob() = {

    logger.info("Execution started with following configuration")

    //implicit statisticsRecordEncoder = Encoders.product[StatisticsRecord]
    val protobufDeserializerUDF = udf { bytes: Array[Byte] => TrackingPacket.parseFrom(bytes) }

    import sparkSession.implicits._
    val decodedRows = sparkSession.readStream
      .format("kafka")
      .option("subscribe", "inputtopic")
      .option("kafka.bootstrap.servers", "ks1:9092,ks2:9092,ks3:9092")
      .option("startingOffsets", "earliest")
      .load()
      .select(protobufDeserializerUDF($"value") as "decoded_value")

    /* Notes on previous code block: 
    ---------------------------------

    .option("startingOffsets", "earliest") 
    can be replaced by 
    .option("startingOffsets", "latest") 
    for instance. cf https://databricks.com/blog/2017/04/26/processing-data-in-apache-kafka-with-structured-streaming-in-apache-spark-2-2.html
    
    sources of inspiration: 
    - http://stackoverflow.com/questions/43934180/how-to-deserialize-records-from-kafka-using-structured-streaming-in-java
    - https://databricks.com/blog/2017/04/26/processing-data-in-apache-kafka-with-structured-streaming-in-apache-spark-2-2.html
    */


    val query = decodedRows
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "ks1:9092,ks2:9092,ks3:9092")
      .option("topic", "debugtopic")
      .option("checkpointLocation", "/somehdfsfolder")
      .start()

    query.awaitTermination()
    sparkSession.stop()
  }
}