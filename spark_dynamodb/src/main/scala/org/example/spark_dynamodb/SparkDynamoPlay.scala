package org.example.spark_dynamodb

import com.audienceproject.spark.dynamodb.implicits._
import com.typesafe.scalalogging.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}
import db.driver.dynamodb.DynamoDbDriver

class SparkDynamoPlay

object SparkDynamoPlay {

  def main(args: Array[String]): Unit = {

    val logger = Logger[SparkDynamoPlay]

    logger.info("Running the application")

    val conf = new SparkConf()
      .setAppName("SparkDynamoApp")
      .setIfMissing("spark.master", "local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val someData = Seq(
      Row(8, "bat"),
      Row(64, "mouse"),
      Row(-27, "horse")
    )

    val someSchema = List(
      StructField("number", IntegerType, true),
      StructField("word", StringType, true)
    )
    logger.info("creating the dataframe")

    val someDF = spark.createDataFrame(
      spark.sparkContext.parallelize(someData),
      StructType(someSchema)
    )

    logger.info(s"${someDF.take(2)}")

    DynamoDbDriver.createTable("SomeTableName", "number")

    someDF.write.option("region","us-east-1").dynamodb("SomeTableName")

    logger.info("dataframe written to dynamoDb table")
    val dynamoDf = spark.read.option("region", "us-east-1").dynamodb("SomeTableName")

    logger.info("dataFrame fetched....")
    logger.info(s"${dynamoDf.take(3)}")

    spark.stop()

  }

}
