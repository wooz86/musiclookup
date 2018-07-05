#!/bin/sh

export GRADLE_USER_HOME="./source-code/.gradle"

chmod +x $GRADLE_USER_HOME

./source-code/gradlew build
