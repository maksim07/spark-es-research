package com.gh.research.spark

import com.typesafe.config.ConfigFactory
import org.apache.spark.streaming.{Seconds, StreamingContext}
import StreamingContext._
import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf
import org.elasticsearch.hadoop.mr.EsOutputFormat
import org.elasticsearch.hadoop.cfg.ConfigurationOptions;
// Hadoop imports

import org.apache.hadoop.mapred.{FileOutputCommitter, FileOutputFormat, JobConf, OutputFormat}
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{MapWritable, Text, NullWritable}

/**
 *
 * @author Max Osipov
 */
object TwitterWordsCount extends App {

  val conf = ConfigFactory.load()
  val esNodes = conf.getString("es.nodes")


    SharedIndex.setupTwitter(conf.getString("twitter.consumerKey"),
    conf.getString("twitter.consumerSecret"),
    conf.getString("twitter.accessToken"),
    conf.getString("twitter.accessTokenSecret"))

  val ssc = new StreamingContext(conf.getString("spark.master"), "NetworkWordCount", Seconds(conf.getInt("app.interval")))

  val tweets = TwitterUtils.createStream(ssc, None)
  tweets.print()
  tweets.foreachRDD { (tweetRDD, time) =>
    val sc = tweetRDD.context
    val jobConf = SharedESConfig.setupEsOnSparkContext(sc, conf.getString("es.resource"), Some(esNodes))
    val tweetsAsMap = tweetRDD.map(SharedIndex.prepareTweets)
    tweetsAsMap.saveAsHadoopDataset(jobConf)
  }
  println("pandas: sscstart")
  ssc.start()
  println("pandas: awaittermination")
  ssc.awaitTermination()
  println("pandas: done!")

}

