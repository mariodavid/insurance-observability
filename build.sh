#!/bin/bash

set -e

rm -rf ./policy-app/build/libs/
./gradlew :policy-core:policy-core:clean
./gradlew :policy-app:clean
./gradlew -Pvaadin.productionMode=true :policy-app:bootJar --no-build-cache

rm -rf ./quote-app/build/libs/
./gradlew :quote-core:quote-core:clean
./gradlew :quote-app:clean
./gradlew -Pvaadin.productionMode=true :quote-app:bootJar --no-build-cache

rm -rf ./account-app/build/libs/
./gradlew :account-core:account-core:clean
./gradlew :account-app:clean
./gradlew -Pvaadin.productionMode=true :account-app:bootJar --no-build-cache


