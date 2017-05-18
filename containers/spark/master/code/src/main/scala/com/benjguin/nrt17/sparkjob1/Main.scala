package com.benjguin.nrt17.sparkjob1

import java.sql.Timestamp
import java.text.{DateFormat, SimpleDateFormat}

import collection.JavaConversions._
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._ 

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

    import sparkSession.implicits._
    val lines = sparkSession.readStream
      .format("kafka")
      .option("subscribe", "inputtopic")
      .option("kafka.bootstrap.servers", "ks1:9092,ks2:9092,ks3:9092")
      .option("startingOffsets", "latest")
      .load()
      .selectExpr("CAST(value AS STRING)" )
      .as[String]

    val df =
      lines.map { line =>
        val columns = line.split("\\|") 
        (columns(0), columns(1), columns(2), columns(3), columns(4).toInt, columns(5))
      }.toDF(cols: _*)
    
      // Deduplicate 
      //  val df = dfi.dropDuplicates()

    df.printSchema()

    val agg = df
                .groupBy($"device_id",$"category", $"timestamp".as("window_time"))
                .agg(sum($"measure1").as("m1_sum_downstream"),sum($"measure2").as("m2_sum_downstream"))

    // Run your business logic here
    val ds = agg
              .select($"device_id", $"category", $"window_time", $"m1_sum_downstream".cast("BigInt"), $"m2_sum_downstream".cast("Double")).as[Commons.UserEvent]

    // This Foreach sink writer writes the output to cassandra.
    import org.apache.spark.sql.ForeachWriter
    val writer = new ForeachWriter[Commons.UserEvent] {
      override def open(partitionId: Long, version: Long) = true
      override def process(value: Commons.UserEvent) = {
        //processRow(value)
      }
      override def close(errorOrNull: Throwable) = {}
    }

    val query =
      ds.writeStream.queryName("aggregateStructuredStream").outputMode("complete").foreach(writer).start

    query.awaitTermination()
    sparkSession.stop()
  }
}