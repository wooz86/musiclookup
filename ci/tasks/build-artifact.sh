#!/usr/bin/env bash
set -e

export GRADLE_OPTS=-Dorg.gradle.native=false
version=`cat version/number`

cd source-code
./gradlew assemble -PprojVersion=$version

cp build/libs/* ../build-artifact-output
