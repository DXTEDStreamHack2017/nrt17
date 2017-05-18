resolvers in ThisBuild ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "nrt17-spark-job1"
version := "0.1"
scalaVersion := "2.11.8"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

val sparkDependencies = {
  val sparkV = "2.1.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkV,
    "org.apache.spark" %% "spark-streaming" % sparkV,
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkV,
    "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkV,
    "org.apache.spark" %% "spark-sql" % sparkV,
    "org.apache.spark" %% "spark-hive" % sparkV,
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "com.holdenkarau" %% "spark-testing-base" % "2.0.0_0.4.4",
    "com.google.guava" % "guava" % "19.0"
  )
}

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= sparkDependencies
  )

mainClass in assembly := Some("com.benjguin.nrt17.sparkjob1.Main")

// make run command include the provided dependencies
run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(
  includeScala = false,
  includeDependency = true
)

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)
// If you need scalapb/scalapb.proto or anything from google/protobuf/*.proto
libraryDependencies += "com.trueaccord.scalapb" %% "scalapb-runtime" % com.trueaccord.scalapb.compiler.Version.scalapbVersion % "protobuf"


parallelExecution in Test := false

javaOptions ++= Seq("-Xms512M",
                    "-Xmx2048M",
                    "-XX:MaxPermSize=2048M",
                    "-XX:+CMSClassUnloadingEnabled")

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "log4j.properties" => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") =>
    MergeStrategy.filterDistinctLines
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}
