make producer:
	docker exec -it java mvn exec:java -Dexec.mainClass=kafka.TwitterProducer
