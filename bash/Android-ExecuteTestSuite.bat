@echo off
set project_dir=%cd%
set lib_Path=%project_dir%\target\lib\
set java_jdk_path=C:\Program Files\Java\jdk1.8.0_171
set dateAndTime=%date:~-4%%date:~4,2%%date:~7,2%_%time:~0,2%%time:~3,2%%time:~6,2%%time:~-2%
set path=%path%;%java_jdk_path%;java_jdk_path\bin;
set classpath=%classpath%;%cd%
set classpath=%classpath%;%project_dir%\target\test-classes
set classpath=%classpath%;%cd%\Config\Log4j2.xml
set classpath=%classpath%;%cd%\Config

if NOT exist ExecutionLogs (mkdir ExecutionLogs)
SETLOCAL ENABLEDELAYEDEXPANSION
for %%i in (%lib_Path%*.jar) do set classpath=!classpath!;%%i

echo Execution started..
echo Kindly check Console log ExecutionLogs\Console_%dateAndTime%.log
title Console_%dateAndTime%

java sqs.base.SQSDriver >ExecutionLogs\Console_%dateAndTime%.log 2>&1

echo Execution Completed.
exit
