package com.gh.research.spark

import com.typesafe.config.ConfigFactory
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext._
import org.apache.hadoop.io.{Text, MapWritable, NullWritable}
import org.apache.spark.streaming.twitter.TwitterUtils

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

  val esResource = conf.getString("es.resource")
  val texts = TwitterUtils.createStream(ssc, None) //ssc.socketTextStream("127.0.0.1", 9191)
  texts.foreachRDD((rdd, time) => {

    val col = rdd.map(text => {


      val m = new MapWritable
      m.put(new Text("message"), new Text(text.getText))

      (NullWritable.get, m)
  })

  val count = col.count()
  println(s"Time is $time with count $count")
  val jobConf = SharedESConfig.setupEsOnSparkContext(rdd.context, esResource, Some(esNodes))
  println(s"jobConf = $jobConf")
  col.saveAsHadoopDataset(jobConf)
}

)


ssc.start ()
ssc.awaitTermination ()
}

