# PolyStates
Map latitude to longitude in scala. Works for US States. Mostly a toy, but we used this when trying to classify what state a tweet came from based on its words.

This uses a ray casting algorithm which is described nicely [here](http://en.wikipedia.org/wiki/Point_in_polygon).

Example of a "costly map task" in Spark that may benefit from caching.

## Building

	sbt/sbt assembly

## Running

Simple Spark invocation:

	export SPARK_HOME=/path/to/spark
	./run-spark.sh local[4] SparkPolyState
