#!/usr/bin/env bash
set -e

export GRADLE_OPTS=-Dorg.gradle.native=false

cd source-code

gradlew -v
gradlew clean test
