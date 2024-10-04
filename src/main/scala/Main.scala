import scala.math.random
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.functions._
import com.google.cloud.spark.bigquery._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.ml.linalg.SQLDataTypes._
import org.apache.spark.sql.{DataFrame, SparkSession}

object Main {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder
      .appName("Spark Window")
      .config("spark.sql.session.timeZone", "America/New_York")
      // .config("spark.master", "local[*]") // local dev
      // .config("spark.log.level", "ERROR") // local dev
      // .config(
      //   "spark.hadoop.fs.AbstractFileSystem.gs.impl",
      //   "com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS"
      // )
      // .config("spark.hadoop.fs.gs.project.id", "cf-data-analytics")
      // .config("spark.hadoop.google.cloud.auth.service.account.enable", "true")
      // .config(
      //   "spark.hadoop.google.cloud.auth.service.account.json.keyfile",
      //   "/Users/chasf/Desktop/cf-data-analytics-c0c7b23bcaf4.json"
      // )
      .getOrCreate()

    spark.sparkContext.setCheckpointDir("gs://cf-spark-temp/checkpoint")

    // val df =
    //   spark.read
    //     .bigquery("cf-data-analytics.market_data.googl")

    // val df2 = df
    //   .groupBy("dt")
    //   .agg(max("trade_price"))
    //   .orderBy("dt")
    //   .withColumnRenamed("max(trade_price)", "max_price")

    // val df3 =
    //   spark.read
    //     .format("parquet")
    //     .load("gs://analytics-data-lake/googl-data/*.parquet")

    // val df2 = df.select(
    //   col("symbol"),
    //   col("latestTrade.t").alias("datetime"),
    //   unix_timestamp(col("latestTrade.t")).alias("tm"),
    //   to_date(col("latestTrade.t")).alias("dt"),
    //   col("latestTrade.x").alias("exchange_code"),
    //   col("latestTrade.p").alias("trade_price"),
    //   col("latestTrade.s").alias("trade_size"),
    //   col("latestTrade.i").alias("trade_id"),
    //   col("latestTrade.z").alias("tape")
    // )

    // df2.write
    //   .format("parquet")
    //   .mode("overwrite")
    //   .save("gs://analytics-data-lake/googl-data/")

    // Create Initial Cloud Storage Files

    val df =
      spark.read
        .format("parquet")
        .load("gs://analytics-data-lake/googl-data/*.parquet")

    val df2 = df
      .groupBy("dt")
      .agg(max("trade_price"))
      .orderBy("dt")
      .withColumnRenamed("max(trade_price)", "max_price")

    df2.show()

    df2.printSchema()

    df2.write
      .format("bigquery")
      // .option(
      //   "temporaryGcsBucket",
      //   "cf-spark-temp"
      // ) // indirect mode destination gcs bucket
      .option("writeMethod", "direct")
      .mode("overwrite") // overwrite or append to destination table
      .save(
        "cf-data-analytics.composer_destination.googl_spark_summarized"
      ) // define destination table

    print("done")

  }
}
