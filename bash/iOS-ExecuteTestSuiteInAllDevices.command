#!/bin/sh

current_directory=$(dirname "$0")
cd $current_directory
clear

idevice_id -l

for deivice in `idevice_id -l`; do
 open ./iOS-ExecuteTestSuite.command
done



