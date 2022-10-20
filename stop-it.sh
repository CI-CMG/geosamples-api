#!/bin/bash

set -ex

docker stop geosamples-api-test-1
docker container rm geosamples-api-test-1
