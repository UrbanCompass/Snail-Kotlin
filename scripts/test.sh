#!/bin/sh
set -e

./gradlew test
./gradlew lint
