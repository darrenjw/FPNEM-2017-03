## First Spark shell commands

Start a Spark shell in the top level of the Spark distribution (where the `README.md` file is), and follow along with the commands.

### Counting lines in a file

We are now ready to start using Spark. From a Spark shell in the top-level directory, enter:
```scala
sc.textFile("README.md").count
```
If all goes well, you should get a count of the number of lines in the file `README.md`. The value `sc` is the "Spark context", containing information about the Spark cluster (here it is just a laptop, but in general it could be a large cluster of machines, each with many processors and each processor with many cores). The `textFile` method loads up the file into an RDD (Resilient Distributed Dataset). The RDD is the fundamental abstraction provided by Spark. It is a lazy distributed parallel monadic collection. After loading a text file like this, each element of the collection represents one line of the file. I've talked about monadic collections in [my previous meetup session](https://github.com/darrenjw/FPNEM-2016-04), so if this isn't a familiar concept, it might be worth having a quick skim through that session (or at least the post on [first steps with monads in Scala](https://darrenjw.wordpress.com/2016/04/15/first-steps-with-monads-in-scala/)). The point is that although RDDs are potentially huge and distributed over a large cluster, using them is very similar to using any other monadic collection in Scala. We can unpack the previous command slightly as follows:
```scala
val rdd1 = sc.textFile("README.md")
rdd1
rdd1.count
```
Note that RDDs are "lazy", and this is important for optimising complex pipelines. So here, after assigning the value `rdd1`, no data is actually loaded into memory. All of the actual computation is deferred until an "action" is called - `count` is an example of such an action, and therefore triggers the loading of data into memory and the counting of elements.

### Counting words in a file

We can now look at a very slightly more complex pipeline - counting the number of words in a text file rather than the number of lines. This can be done as follows:
```scala
sc.textFile("README.md").
  map(_.trim).
  flatMap(_.split(' ')).
  count
```
Note that `map` and `flatMap` are both lazy ("transformations" in Spark terminology), and so no computation is triggered until the final action, `count` is called. The call to `map` will just trim any redundant white-space from the line ends. So after the call to `map` the RDD will still have one element for each line of the file. However, the call to `flatMap` splits each line on white-space, so after this call each element of the RDD will correspond to a word, and not a line. So, the final `count` will again count the number of elements in the RDD, but here this corresponds to the number of words in the file.

### Counting character frequencies in a file

A final example before moving on to look at Twitter data: counting the frequency with which each character occurs in a file. This can be done as follows:
```scala
sc.textFile("README.md").
  map(_.toLowerCase).
  flatMap(_.toCharArray).
  map{(_,1)}.
  reduceByKey(_+_).
  collect
```
The first call to `map` converts upper case characters to lower case, as we don't want separate counts for upper and lower case characters. The call to `flatMap` then makes each element of the RDD correspond to a single character in the file. The second call to `map` transforms each element of the RDD to a key-value pair, where the key is the character and the value is the integer 1. RDDs have special methods for key-value pairs in this form - the method `reduceByKey` is one such - it applies the reduction operation (here just "+") to all values corresponding to a particular value of the key. Since each character has the value 1, the sum of the values will be a character count. Note that the reduction will be done in parallel, and for this to work it is vital that the reduction operation is associative. Simple addition of integers is clearly associative, so here we are fine. Note that `reduceByKey` is a (lazy) transformation, and so the computation needs to be triggered by a call to the action `collect`.

On most Unix-like systems there is a file called `words` that is used for spell-checking. The example below applies the character count to this file. Note the calls to `filter`, which filter out any elements of the RDD not matching the predicate. Here it is used to filter out special characters.
```scala
sc.textFile("/usr/share/dict/words").
  map(_.trim).
  map(_.toLowerCase).
  flatMap(_.toCharArray).
  filter(_ > '/').
  filter(_ < '}').
  map{(_,1)}.
  reduceByKey(_+_).
  collect
```



