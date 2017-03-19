# Commonly used Spark methods

## Transformations of an `RDD[T]`

* distinct
* filter
* flatMap
* map
* persist
* sample
* sortBy
* zip

## Actions on an `RDD[T]`

* aggregate
* collect
* count
* fold
* foreach
* reduce
* take

## Transformations of a Pair RDD, `RDD[(K,V)]`

* aggregateByKey
* groupByKey
* keys
* mapValues
* reduceByKey
* values

## Actions on a `RDD[(K,V)]`

* countByKey

## `DataFrames`

* toDF
* col
* drop
* select
* show
* withColumn

# API Docs

* [RDD API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.rdd.RDD)
* [Pair RDD API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.rdd.PairRDDFunctions)
* [Dataset API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.sql.Dataset)
  * [DataFrameReader API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.sql.DataFrameReader)

#### eof


