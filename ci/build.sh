#!/bin/sh

cd source-code

export ROOT_FOLDER=$(pwd)
export GRADLE_USER_HOME="${ROOT_FOLDER}/.gradle"

./gradlew clean build
