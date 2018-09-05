#!/bin/bash
cd enforcer
mvn clean 
if [ $? -eq 0 ]; then
cd ..
else
cd ..
cd builder
mvn clean 
cd ..
fi
