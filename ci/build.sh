#!/bin/sh

export GRADLE_USER_HOME="./source-code/.gradle"

./source-code/gradlew wrapper
./source-code/gradlew clean build
