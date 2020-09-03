#!/bin/bash

set -e

IMAGE_NAME=$(docker images | grep javabom | awk '{print $3}')

if [ -n "$IMAGE_NAME" ] ; then
  echo ">> delete exist image"
  for IMAGE in $IMAGE_NAME
  do
    echo ">> delete image : $IMAGE"
    docker rmi -f $IMAGE
  done
else
  echo ">> not exist deletable image"
fi
