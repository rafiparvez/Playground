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

    val region = "us-east-1"

    val logger = Logger[SparkDynamoPlay]

    logger.info("Running the application")

    val conf = new SparkConf()
      .setAppName("SparkDynamoApp")
      .setIfMissing("spark.master", "local[*]")
    val spark = SparkSession.builder().config(conf).getOrCreate()

    val someData = Seq(
      Row(18, "alice", 4.2 ),
      Row(31, "bob", 2.3),
      Row(27, "charlie", 1.1)
    )

    val someSchema = List(
      StructField("id", IntegerType, nullable = true),
      StructField("name", StringType, nullable = true),
      StructField("score", DoubleType, nullable = true)
    )
    logger.info("creating the dataframe")

    val someDF = spark.createDataFrame(
      spark.sparkContext.parallelize(someData),
      StructType(someSchema)
    )

    logger.info(s"${someDF.take(2)}")

    val dynamoDBInstance = DynamoDbDriver()

    import dynamoDBInstance.dynamoDBClient

    dynamoDBInstance.deleteTable(SomeTable.tableName)
    dynamoDBInstance.createTable(SomeTable)

    someDF.write.option("region", region).dynamodb(SomeTable.tableName)

    logger.info("dataframe written to dynamoDb table")
    val dynamoDf = spark.read.option("region", region).dynamodb(SomeTable.tableName)

    logger.info("dataFrame fetched....")
    logger.info(s"${dynamoDf.take(3)}")

    spark.stop()

  }

}
