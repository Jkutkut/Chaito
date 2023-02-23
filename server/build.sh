#!/bin/bash

if [ "$1" = "--reset" ]; then
    echo "Removing old docker image..."
    docker rmi jkutkut/chaito_server
fi

echo "Building docker image..."
docker build -t jkutkut/chaito_server .
