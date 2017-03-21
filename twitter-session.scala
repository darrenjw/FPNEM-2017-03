/*
twitter-session.scala

Spark shell session for the analysis of twitter data

spark-shell --master local[4] --driver-memory 2G

*/

val df1 = spark.read.
  option("header","true").
  option("inferSchema","true").
  csv("UO-Twitter-1m.csv.gz").persist

df1 show 2
df1.count
df1.schema
df1.printSchema

val df2 = df1.select("value","x5","y","screen_name")
df2.printSchema
df2 show 5 

import org.apache.spark.sql.types._
val df3 = df2.
  withColumn("long",df2("x5").cast(DoubleType)).drop("x5").
  withColumn("lat",df2("y").cast(DoubleType)).drop("y").
  withColumnRenamed("value","tweet").
  withColumnRenamed("screen_name","sname").
  filter("long is! null").
  filter("lat is! null")
df3.printSchema
df3.show(5)

val df4 = df3.
  explode("tweet","word")((tweet: String) => tweet.split(" ")).
  drop("tweet")
df4.count
df4.show(10)

case class Tweet(tweet: String, sname: String,
  long: Double, lat: Double)

import org.apache.spark.sql.Row

val rdd = df3.rdd.map{
  case Row(tweet: String, sname: String,
    long: Double, lat: Double) =>
    Tweet(tweet,sname,long,lat)
  case Row(tweet: String, _, long: Double, lat: Double) =>
    Tweet(tweet,"",long,lat)
}
rdd.count
rdd take 10

val rdd2 = rdd.flatMap{t => t.tweet.split(" ").
  map(word => Tweet(word,t.sname,t.long,t.lat))}
rdd2.count
rdd2.take(10)

val rdd3 = rdd2.
  filter(_.tweet.length > 0).
  filter(_.tweet(0) == '#').persist
rdd3.count
rdd3 take 10




// eof


