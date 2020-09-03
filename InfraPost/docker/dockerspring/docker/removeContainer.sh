#!/bin/bash

set -e

CONTAINER_NAME=$(docker ps -a | grep javabom | awk '{print $1}')

if [ -n "$CONTAINER_NAME" ] ; then
  echo ">> stop container"
  docker stop "$CONTAINER_NAME"
  echo ">> delete exist container"
  docker rm "$CONTAINER_NAME"
else
  echo ">> not exist deletable container"
fi
