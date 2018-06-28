#!/bin/sh

export ROOT_FOLDER=$(pwd)
export GRADLE_USER_HOME="${ROOT_FOLDER}/.gradle"

./gradlew clean build
