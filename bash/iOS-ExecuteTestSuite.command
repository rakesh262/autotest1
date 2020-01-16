#!/bin/sh
current_directory=$(dirname "$0")
cd $current_directory
clear



java -cp "$current_directory/target/test-classes:$current_directory/target/lib/*:" SQSDriver

