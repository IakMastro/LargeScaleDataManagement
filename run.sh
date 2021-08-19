#!/bin/sh

docker-compose up -d --force-recreate namenode \
                            datanode nodemanager \
                            historyserver java

sleep 5 
docker exec -it namenode hdfs dfsadmin -safemode leave

docker-compose up -d --force-recreate resourcemanager

namenode_ip=$(docker inspect namenode | jq '.[].NetworkSettings.Networks.largescaledatamanagement_default.IPAddress' | sed 's/\"//g')
echo "Namenode address: http://${namenode_ip}:9870"
