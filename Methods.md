# Commonly used Spark methods

Remember that **transformations** are *lazy* and **actions** are *strict*. An action must be called to trigger computation.

## Transformations of an `RDD[T]`

```scala
def distinct: RDD[T]
def filter(f: (T) => Boolean): RDD[T]
def flatMap[U](f: (T) => TraversableOnce[U]): RDD[U]
def map[U](f: (T) => U): RDD[U]
def persist: RDD[T]
def sample(withReplacement: Boolean, fraction: Double): RDD[T]
def sortBy[K](f: (T) ⇒ K, ascending: Boolean = true): RDD[T]
def zip[U](other: RDD[U]): RDD[(T, U)]
```

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


