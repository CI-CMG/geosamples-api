#!/bin/bash

set -ex

if [[ -z "$MB_WORK_DIR" ]]; then
   MB_WORK_DIR="$(pwd)/test-dir/work"
fi


docker run -d --name geosamples-api-test-1 -p 15215:1521 cirescmg/crowbar-db:20210416