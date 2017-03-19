/*
twitter-session.scala

Spark shell session for the analysis of twitter data

spark-shell --master local[4] --driver-memory 2G

*/

val df1 = spark.read.
	option("header","true").
	option("inferSchema","true").
	csv("UO-Twitter-1m.csv.gz").persist

df1.show(2)
df1.count
df1.schema
df1.printSchema

val df2 = df1.select("value","x5","y").persist
df2.printSchema
df2.show(5)

import org.apache.spark.sql.types._
val df3 = df2.
	withColumn("long",df2("x5").cast(DoubleType)).drop("x5").
	withColumn("lat",df2("y").cast(DoubleType)).drop("y").
	withColumnRenamed("value","tweet").
	filter("long is! null").
	filter("lat is! null")
df3.printSchema
df3.show(5)

val df4 = df3.
	explode("tweet","word")((tweet: String) => tweet.split(" ")).
	drop("tweet")
df4.count
df4.show(10)

import org.apache.spark.sql.Row
val rdd = df3.rdd.map{case Row(tweet: String,long: Double,lat: Double) => (tweet,long,lat)}
rdd.count
rdd.take(10)

val rdd2 = rdd.flatMap{tup => tup._1.split(" ").map(word => (word,tup._2,tup._3))}
rdd2.count
rdd2.take(10)

val rdd3 = rdd2.
	filter(_._1.length > 0).
	filter(_._1(0) == '#')
rdd3.count
rdd3.take(10)


// 54.95 lat cuts below the Tyne?

// Sunderland station: 54.905616, -1.382348 (lat,long)
// Tweets within 5 miles?


// eof


