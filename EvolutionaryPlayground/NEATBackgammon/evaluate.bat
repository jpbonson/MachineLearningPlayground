@echo off
set MYCLASSPATH=.\lib\anji.jar;.\lib\jgap.jar;.\lib\log4j.jar;.\lib\jakarta-regexp-1.3.jar;.\lib\clibwrapper_jiio.jar;.\lib\mlibwrapper_jiio.jar;.\lib\jai_imageio.jar;.\lib\hb16.jar;.\lib\jcommon.jar;.\lib\jfreechart.jar;.\lib\jakarta-regexp-1.3.jar;.\properties
java -classpath %MYCLASSPATH% -Xmx6g -Xms6g -d64 -XX:MaxHeapSize=6g com.anji.integration.Evaluator %1 %2 %3 %4 %5 %6 %7 %8 %9

