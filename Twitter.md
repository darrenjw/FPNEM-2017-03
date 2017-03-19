# Analysis of Twitter data

Newcastle's [Urban Observatory](https://research.ncl.ac.uk/urbanobservatory/) is collecting data from a variety of sensors around Newcastle. As well as interactive web visualisation, there is an API allowing programmatic access to data. Twitter data that mentions Newcastle, and geotagged tweets in the North East are also collected. For this session we will look at 1 million tweets collected from the UO.

**Instructions on how to download the data will be provided at the meetup**

The data is in a compressed CSV file (with a header row), with each row corresponding to a single tweet.

## Interactive session

Start by reading the data into a Spark DataFrame.

```scala
val df1 = spark.read.
	option("header","true").
	option("inferSchema","true").
	csv("UO-Twitter-1m.csv.gz").persist

df1.show(2)
df1.count
df1.schema
df1.printSchema
```
Note that the schema inference (requiring a second pass over the data) hasn't worked very well here. For this exercise we only care about the tweet text and the geo-location (if available). So let's pull those out.
```scala
val df2 = df1.select("value","x5","y").persist
df2.printSchema
df2.show(5)
```
Next we can rename and re-type the columns, and filter out any tweets non geo-tagged.
```scala
import org.apache.spark.sql.types._
val df3 = df2.
	withColumn("long",df2("x5").cast(DoubleType)).drop("x5").
	withColumn("lat",df2("y").cast(DoubleType)).drop("y").
	withColumnRenamed("value","tweet").
	filter("long is! null").
	filter("lat is! null")
df3.printSchema
df3.show(5)
```
So far so good. Now we want to look at the hashtags within tweets. For this we need to split the text of the tweets into words. The now-deprecated `explode` method does this for us.
```scala
val df4 = df3.
	explode("tweet","word")((tweet: String) => tweet.split(" ")).
	drop("tweet")
df4.count
df4.show(10)
```
This works, but is deprecated, so this isn't very satisfactory. There's presumably a better way to do this now, but I haven't yet figured it out. However, for a `DataFrame` as simple as this, we could just convert back to an `RDD` and work with that instead.
```scala
import org.apache.spark.sql.Row
val rdd = df3.rdd.map{case Row(tweet: String,long: Double,lat: Double) => (tweet,long,lat)}
rdd.count
rdd.take(10)
```
Now we have an `RDD[(String,Double,Double)]` we can just `flatMap`...
```scala
val rdd2 = rdd.flatMap{tup => tup._1.split(" ").map(word => (word,tup._2,tup._3))}
rdd2.count
rdd2.take(10)
```
This looks fine. So now let's just concentrate on the hashtags.
```scala
val rdd3 = rdd2.
	filter(_._1.length > 0).
	filter(_._1(0) == '#')
rdd3.count
rdd3.take(10)
```
So there are 200k geotagged hashtag usages.

## Exercises

1. What are the top 10 most popular hashtags?
2. The latitude 54.95 cuts below the Tyne. How does hashtag popularity differ above and below this cutoff?
3. Sunderland railway station has latitude 54.905616 and longitude -1.382348. What are the top 10 most popular hashtags within a 5 miles radius of this?

For 3., you probably want to use the [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula) which you can grab in Scala from the end of [this blog post](https://davidkeen.com/blog/2013/10/calculating-distance-with-scalas-foldleft/).



#### eof



