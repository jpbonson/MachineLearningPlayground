#!/bin/bash
#echo off
export MYCLASSPATH=./properties
for i in `ls ./lib/*.jar`
do 
	export MYCLASSPATH=${MYCLASSPATH}:${i}
done
echo ${MYCLASSPATH}
nohup java -classpath ${MYCLASSPATH} -Xms2g -Xmx2g -XX:MaxHeapSize=2g com.anji.neat.Evolver $1 &
