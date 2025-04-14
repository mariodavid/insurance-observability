#!/bin/bash

set -e

./gradlew :policy-core:policy-core:clean :policy-app:clean
./gradlew -Pvaadin.productionMode=true :policy-app:bootJar --no-build-cache

./gradlew :quote-core:quote-core:clean :quote-app:clean
./gradlew -Pvaadin.productionMode=true :quote-app:bootJar --no-build-cache

./gradlew :account-core:account-core:clean :account-app:clean
./gradlew -Pvaadin.productionMode=true :account-app:bootJar --no-build-cache

./gradlew :partner-core:partner-core:clean :partner-app:clean
./gradlew -Pvaadin.productionMode=true :partner-app:bootJar --no-build-cache

./gradlew :txo-publisher-core:txo-publisher-core:clean :txo-publisher-app:clean
./gradlew -Pvaadin.productionMode=true :txo-publisher-app:bootJar --no-build-cache