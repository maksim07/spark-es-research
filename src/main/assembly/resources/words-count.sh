#!/bin/bash

sdir=`dirname $0`

java -Xmx2048m -cp $sdir/classes:$sdir/libs/* com.gh.research.spark.WordsCount $@




