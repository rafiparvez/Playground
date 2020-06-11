package com.helloWorld.spark

import scala.math.random

import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/** Computes an approximation to pi */
object SparkPi {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .appName("Spark Pi")
      .getOrCreate()
    val slices = if (args.length > 1) args(1).toInt else 2
    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    val count = spark.sparkContext.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y <= 1) 1 else 0
    }.reduce(_ + _)

    val pi_val = 4.0 * count / (n - 1)
    println(s"Pi is roughly ${pi_val}")

    val stringRdd = spark.sparkContext.parallelize(Seq(s"Pi is roughly ${pi_val}"))
    stringRdd.saveAsTextFile(args(0))

  }
}
