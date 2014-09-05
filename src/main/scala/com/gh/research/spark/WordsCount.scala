package com.gh.research.spark

import org.apache.spark.{SparkContext, SparkConf}

/**
 *
 * @author Max Osipov
 */
object WordsCount extends App {

  assume(args.size > 0, "USAGE: java com.gh.research.spark.WordCount <text-file>")

  val conf = new SparkConf().setAppName("word-count").setMaster("local")
  val spark = new SparkContext(conf)

  val rdd = spark.textFile(args(0), 4)

  val words = rdd.flatMap(_.split("\\s")).persist()
  val overallCount = words.count()
  val shortsCount = words.filter(_.length <= 4).count()

  println(s"Count of words in file is $overallCount and shorts count is $shortsCount")
}
