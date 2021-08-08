#!/bin/sh

docker-compose up -d
docker exec -it namenode hadoop dfsadmin -safemode leave