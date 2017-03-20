# Commonly used Spark methods

Remember that **transformations** are *lazy* and **actions** are *strict*. An action must be called to trigger computation.

## Transformations of an `RDD[T]`

```scala
def ++(other: RDD[T]): RDD[T]
def cartesian[U](other: RDD[U]): RDD[(T, U)]
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

```scala
def aggregate[U](zeroValue: U)(seqOp: (U, T) => U, combOp: (U, U) => U): U
def collect: Array[T]
def count: Long
def fold(zeroValue: T)(op: (T, T) => T): T
def foreach(f: (T) => Unit): Unit
def reduce(f: (T, T) => T): T
def take(num: Int): Array[T]
```

## Transformations of a Pair RDD, `RDD[(K,V)]`

```scala
def aggregateByKey[U](zeroValue: U)(seqOp: (U, V) => U, combOp: (U, U) => U): RDD[(K, U)]
def groupByKey(): RDD[(K, Iterable[V])]
def join[W](other: RDD[(K, W)]): RDD[(K, (V, W))]
def keys: RDD[K]
def mapValues[U](f: (V) => U): RDD[(K, U)]
def reduceByKey(func: (V, V) => V): RDD[(K, V)]
def values: RDD[V]
```

## Actions on a `RDD[(K,V)]`

```scala
def countByKey: Map[K, Long]
```

## `DataFrames`

```scala
def toDF(colNames: String*): DataFrame
def col(colName: String): Column
def drop(col: Column): DataFrame
def select(cols: Column*): DataFrame
def show(numRows: Int): Unit
def withColumn(colName: String, col: Column): DataFrame
def withColumnRenamed(existingName: String, newName: String): DataFrame
```

# API Docs

* [RDD API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.rdd.RDD)
* [Pair RDD API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.rdd.PairRDDFunctions)
* [Dataset API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.sql.Dataset)
  * [DataFrameReader API docs](http://spark.apache.org/docs/latest/api/scala/#org.apache.spark.sql.DataFrameReader)

#### eof


