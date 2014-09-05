# Spark and Elasticsearch integration research project

To run you should:

  1. Install Elasticsearch and create index there with name 'twitter'
  2. Build project by: `mvn package`
  3. Copy conf.template.conf somewhere and add there your twitter application credentials (you have to create your application first at http://dev.twitter.org)
  4. Create schema by running script `./target/spark-1.0-SNAPSHOT-distr/spark-1.0-SNAPSHOT/create-es-schema.sh elastic-host:port`
  5. Run application by command `./target/spark-1.0-SNAPSHOT-distr/spark-1.0-SNAPSHOT/twitter.sh <path to conf file (see 3 above)>`
  
