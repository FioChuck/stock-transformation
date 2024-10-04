name := "spark-window-functions"
organization := "chasf"
version := "3.0"

scalaVersion := "2.12.14"

val sparkVersion = "3.1.3"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  // "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  // "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  // "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.6" % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.6",
  // "org.postgresql" % "postgresql" % "42.7.3",
  "com.google.cloud.spark" %% "spark-bigquery-with-dependencies_2.12" % "0.41.0"
  // "io.openlineage" %% "openlineage-spark" % "1.18.0"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x                             => MergeStrategy.first
}
