#!/bin/bash

sdir=`dirname $0`

cf=$1
shift

java -Dconfig.file=$cf -Xmx2048m -cp $sdir/classes:$sdir/libs/* com.gh.research.spark.TwitterWordsCount $@




