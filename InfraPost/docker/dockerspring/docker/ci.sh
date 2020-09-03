#!/bin/bash

set -e

sh ./removeContainer.sh
sh ./removeNone.sh
sh ./removeImage.sh

cd ..
./gradlew clean build
VERSION=$(ls ./build/libs | cut -d '-' -f2 | rev | cut -c 5- | rev)
docker build --build-arg SPRING_PROFILES_ACTIVE=set -t javabom/docker-spring:"$VERSION" -t javabom/docker-spring .
#docker push javabom/docker-spring