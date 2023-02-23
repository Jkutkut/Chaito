#!/bin/bash

docker run -it --rm -v $(pwd)/db:/app/db -p 3232:3232 jkutkut/chaito_server
