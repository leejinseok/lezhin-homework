#!/bin/sh

./gradlew lezhin-api:clean
./gradlew lezhin-api:build

java -jar lezhin-api/build/libs/lezhin-api-0.0.1-SNAPSHOT.jar