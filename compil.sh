#!/bin/bash
cd enforcer
mvn install -DskipTests
if [ $? -eq 0 ]; then
cd ..
else
cd ..
cd builder
mvn install -DskipTests
cd ..
fi
