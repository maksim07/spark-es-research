package com.gh.research.spark

import scala.collection.JavaConversions._
import org.apache.hadoop.io.{MapWritable, Text, NullWritable}


/**
 *
 * @author Max Osipov
 */
object SharedIndex {

  // twitter helper methods
  def prepareTweets(tweet: String) = {
    val output = mapToOutput(Map("message" -> tweet))
    output
  }

  // hadoop helper methods
  def mapToOutput(in: Map[String, String]): (Object, Object) = {
    val m = new MapWritable
    for ((k, v) <- in)
      m.put(new Text(k), new Text(v))
    (NullWritable.get, m)
  }
  def mapWritableToInput(in: MapWritable): Map[String, String] = {
    in.map{case (k, v) => (k.toString, v.toString)}.toMap
  }

  def setupTwitter(consumerKey: String, consumerSecret: String, accessToken: String, accessTokenSecret: String) ={
    // Set up the system properties for twitter
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    // https:  all kinds of fun
    System.setProperty("twitter4j.restBaseURL", "https://api.twitter.com/1.1/")
    System.setProperty("twitter4j.streamBaseURL", "https://stream.twitter.com/1.1/")
    System.setProperty("twitter4j.siteStreamBaseURL", "https://sitestream.twitter.com/1.1/")
    System.setProperty("twitter4j.userStreamBaseURL", "https://userstream.twitter.com/1.1/")
    System.setProperty("twitter4j.oauth.requestTokenURL", "https://api.twitter.com/oauth/request_token")
    System.setProperty("twitter4j.oauth.accessTokenURL", "https://api.twitter.com/oauth/access_token")
    System.setProperty("twitter4j.oauth.authorizationURL", "https://api.twitter.com/oauth/authorize")
    System.setProperty("twitter4j.oauth.authenticationURL", "https://api.twitter.com/oauth/authenticate")
  }

}
