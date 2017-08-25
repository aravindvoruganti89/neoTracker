#!/bin/sh

cd ../

CMDLINEARGS="`pwd`"

export JAVABIN="./jre/bin/java"

$JAVABIN -classpath $CMDLINEARGS/lib/* com.test.ExecuteNeoTracker $CMDLINEARGS