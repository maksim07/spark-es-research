package com.gh.research.spark

import org.apache.spark._
import org.elasticsearch.hadoop.cfg.ConfigurationOptions
import org.apache.hadoop.mapred.{FileOutputCommitter, FileOutputFormat, JobConf}
import org.apache.hadoop.fs.Path


/**
 *
 * @author Max Osipov
 */
object SharedESConfig {
  def setupEsOnSparkContext(sc: SparkContext, esResource: String, esNodes: Option[String] = None,
                            esSparkPartition: Boolean = false) = {
    println("Creating configuration to write to "+esResource+" on "+esNodes)
    val jobConf = new JobConf(sc.hadoopConfiguration)
    jobConf.set("mapred.output.format.class", "org.elasticsearch.hadoop.mr.EsOutputFormat")
    jobConf.setOutputCommitter(classOf[FileOutputCommitter])
    jobConf.set(ConfigurationOptions.ES_RESOURCE, esResource)
    esNodes match {
      case Some(node) => jobConf.set(ConfigurationOptions.ES_NODES, node)
      case _ => // Skip it
    }

    // This tells the ES MR output format to use the spark partition index as the node index
    // This will degrade performance unless you have the same partition layout as ES in which case
    // it should improve performance.
    // Note: this is currently implemented as kind of a hack.
    if (esSparkPartition) {
      jobConf.set("es.sparkpartition", "true")
    }
    FileOutputFormat.setOutputPath(jobConf, new Path("-"))
    jobConf
  }
}
