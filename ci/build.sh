#!/bin/sh

export GRADLE_USER_HOME="./source-code/.gradle"

./source-code/gradlew build -jar.archiveName x.jar
