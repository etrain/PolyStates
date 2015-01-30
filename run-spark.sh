#!/bin/bash

MASTER=$1
CLASS=$2
PROJECT=PolyStates
shift
shift

$SPARK_HOME/bin/spark-submit \
  --master $MASTER \
  --deploy-mode client \
  --class $CLASS \
  --driver-memory 2G \
  --driver-java-options "-Dspark.local.dir=/tmp -XX:+UseG1GC" \
  target/scala-2.10/$PROJECT-assembly-0.1.jar \
  "$@"
