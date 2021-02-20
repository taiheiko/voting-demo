#!/bin/bash

docker container run --rm -ti -p 4040:4040 --env PORT=4040 votingapp:latest
