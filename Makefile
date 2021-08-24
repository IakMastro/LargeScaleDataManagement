build:
	docker exec -it java mvn compile

deserializer:
	docker exec -it java mvn exec:java -Dexec.mainClass=avro.Deserializer

lang:
	docker exec -it java mvn exec:java -Dexec.mainClass=mapreduce.lang_count.LangCountRunner

verified:
	docker exec -it java mvn exec:java -Dexec.mainClass=mapreduce.verified.VerifiedRunner

filter:
	docker exec -it java mvn exec:java -Dexec.mainClass=mapreduce.filter.FilterRunner

location:
	docker exec -it java mvn exec:java -Dexec.mainClass=mapreduce.location.LocationRunner