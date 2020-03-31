#!/bin/bash
set -xe

rm -rf twitterdata/*

if [ "$1" == "test" ]; then
  mvn clean package spring-boot:run -Dspring-boot.run.profiles=local
else
  mvn clean package -Dmaven.test.skip=true spring-boot:run -Dspring-boot.run.profiles=local
fi
