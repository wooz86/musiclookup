#!/bin/sh


export GRADLE_USER_HOME="./source-code/.gradle"
mkdir $GRADLE_USER_HOME
chmod +x $GRADLE_USER_HOME

./source-code/gradlew build
