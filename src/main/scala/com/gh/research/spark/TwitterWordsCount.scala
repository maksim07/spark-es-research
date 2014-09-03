package com.gh.research.spark

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

    if (args.length < 5) {
      System.err.println("Usage TwitterWordsCount <master> <key> <secret key> <access token> <access token secret>  <es-resource> [es-nodes]")
    }
    val Array(consumerKey, consumerSecret, accessToken, accessTokenSecret, esResource) = args.take(5)
    val esNodes = args.length match {
      case x: Int if x > 6 => args(6)
      case _ => "localhost:9200"
    }

    SharedIndex.setupTwitter(consumerKey, consumerSecret, accessToken, accessTokenSecret)

  val ssc = new StreamingContext("local[2]", "NetworkWordCount", Seconds(5))

    val tweets = TwitterUtils.createStream(ssc, None)
    tweets.print()
    tweets.foreachRDD{(tweetRDD, time) =>
      val sc = tweetRDD.context
      val jobConf = SharedESConfig.setupEsOnSparkContext(sc, esResource, Some(esNodes))
      val tweetsAsMap = tweetRDD.map(SharedIndex.prepareTweets)
      tweetsAsMap.saveAsHadoopDataset(jobConf)
    }
    println("pandas: sscstart")
    ssc.start()
    println("pandas: awaittermination")
    ssc.awaitTermination()
    println("pandas: done!")

}

