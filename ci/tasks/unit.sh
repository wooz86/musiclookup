#!/usr/bin/env bash
set -e

export GRADLE_USER_HOME="./source-code/.gradle"
export GRADLE_OPTS=-Dorg.gradle.native=false

./source-code/gradlew -v
./source-code/gradlew test
