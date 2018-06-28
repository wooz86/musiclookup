#!/bin/sh

export ROOT_FOLDER=$(pwd)
export GRADLE_USER_HOME="${ROOT_FOLDER}/.gradle"

./source-code/gradlew wrapper && ./source-code/gradlew clean build
