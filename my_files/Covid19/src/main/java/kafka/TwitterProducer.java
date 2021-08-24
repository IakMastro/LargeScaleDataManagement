package kafka;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.twitter.bijection.avro.GenericAvroCodecs;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    final Logger logger = LoggerFactory.getLogger(TwitterProducer.class);

    private Client client;
    private KafkaProducer<String, byte[]> producer;
    private final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(1000);
    private final List<String> terms = Lists.newArrayList("covid2019", "coronavirus", "vaccinate");
    private final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            new TwitterProducer().run(args[0]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Twitter Client creation
    public Client createClient(BlockingQueue<String> blockingQueue) {
        var hosts = new HttpHosts(Constants.STREAM_HOST); // Twitter hosts
        var endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(terms); // Find the terms covid2019, coronavirus and vaccinate

        // Create the authorization using the keys provided by Twitter.
        // TwitterConfig is a hidden file.
        var auth = new OAuth1(
                TwitterConfig.CONSUMER_KEYS,
                TwitterConfig.CONSUMER_SECRET,
                TwitterConfig.TOKEN,
                TwitterConfig.SECRET
        );

        // Build the client and send it back
        var builder = new ClientBuilder()
                .name("Twitter-Producer")
                .hosts(hosts)
                .authentication(auth)
                .endpoint(endpoint)
                .processor(new StringDelimitedProcessor(blockingQueue));

        return builder.build();
    }

    private KafkaProducer<String, byte[]> createKafkaProducer() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAPSERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        // create safe Producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, KafkaConfig.ACKS_CONFIG);
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, KafkaConfig.RETRIES_CONFIG);
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, KafkaConfig.MAX_IN_FLIGHT_CONN);

        // Additional settings for high throughput producer
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, KafkaConfig.COMPRESSION_TYPE);
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, KafkaConfig.LINGER_CONFIG);
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, KafkaConfig.BATCH_SIZE);

        return new KafkaProducer<>(properties);
    }

    private void run(String fileName) throws IOException, InterruptedException {
        logger.info("Setting up");

        client = createClient(blockingQueue);
        client.connect();

        producer = createKafkaProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Application is not stopping!");
            client.stop();
            logger.info("Closing Producer");
            producer.close();
            logger.info("Finished closing");
        }));

        var schema = new Schema.Parser().parse(new File("schema/twitter.avsc"));

        var writer = new DataFileWriter<GenericRecord>(new GenericDatumWriter<GenericRecord>(schema));
        var recordInjection = GenericAvroCodecs.toBinary(schema);
        var configuration = new Configuration();
        var fileSystem = FileSystem.get(URI.create("hdfs://namenode:9000/Input/".concat(fileName)), configuration);
        var output = fileSystem.create(new Path("hdfs://namenode:9000/Input/".concat(fileName)));
        writer.create(schema, output);
        for (int i = 0; i < 100; i++) {
            String message =  blockingQueue.poll(5, TimeUnit.SECONDS);

            var avroRecord = new GenericData.Record(schema);
            var tweet = gson.fromJson(message, JsonElement.class).getAsJsonObject();
            var extended_tweet = tweet.get("extended_tweet");
            var user = tweet.get("user").getAsJsonObject();

            avroRecord.put("name", String.valueOf(user.get("name")));
            avroRecord.put("location", String.valueOf(user.get("location")));
            avroRecord.put("verified", user.get("verified").getAsBoolean());

            if (extended_tweet != null) {
                avroRecord.put("text", String.valueOf(extended_tweet.getAsJsonObject().get("full_text")));
            } else {
                avroRecord.put("text", String.valueOf(tweet.get("text")));
            }

            avroRecord.put("lang", String.valueOf(tweet.get("lang")));
            avroRecord.put("filter", String.valueOf(tweet.get("filter_level")));

            var bytes = recordInjection.apply(avroRecord);
            var record = new ProducerRecord<String, byte[]>(KafkaConfig.TOPIC, String.valueOf(tweet.get("id_str")), bytes);
            logger.info(String.valueOf(record));
            producer.send(record);
            producer.flush();
            writer.append(avroRecord);
        }
        writer.close();

        logger.info("Application end");
    }
}