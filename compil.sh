#!/bin/bash
cd enforcer
sudo mvn clean install -DskipTests
if [ $? neq 0 ]; then
cd ..
else
cd ..
cd builder
sudo mvn clean install -DskipTests
cd ..
fi
