#!/bin/sh

echo "Opening the containers"
docker-compose up -d kafka flume

echo "Containers are ready."
read -p "Enter file name to save the data: " filename

echo "Start of producing the data."
docker exec -it java mvn exec:java -Dexec.mainClass=kafka.TwitterProducer -Dexec.args="${filename}"

echo "End of produced data. Stopping the containers."
echo "It will take 60 seconds. Please wait..."
docker-compose stop -t 60 kafka zookeeper flume

namenode_ip=$(docker inspect namenode | jq '.[].NetworkSettings.Networks.largescaledatamanagement_default.IPAddress' | sed 's/\"//g')
echo "Done. View the data at: http://${namenode_ip}:9870"