#!/bin/sh

cd /my_files/rest
mvn compile
mvn exec:java -Dexec.mainClass=com.rest_api.rest.RestApplication