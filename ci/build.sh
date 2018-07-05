#!/bin/sh

export GRADLE_USER_HOME="./source-code/.gradle"

cd source-code

./gradlew build
mkdir build-docker

cp Dockerfile build-docker
cp build/libs/* build-docker
