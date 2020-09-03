#!/bin/bash

NONE_IMAGES=`docker images | grep none | awk '{print $3}'`

if [ -z "$NONE_IMAGES" ] ; then
 echo ">> not exist deletable none image"
fi

for IMAGE in $NONE_IMAGES
do
  echo ">> remove image : $IMAGE"
  docker rmi $IMAGE
done