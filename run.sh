#!/bin/sh

./gradlew shadowJar

docker-compose up --build
