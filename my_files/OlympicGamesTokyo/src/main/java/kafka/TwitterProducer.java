package kafka;

import com.google.common.collect.Lists;
import com.twitter.bijection.avro.GenericAvroCodecs;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
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
    private final List<String> terms = Lists.newArrayList("olympic", "games", "tokyo", "2020");

    public static void main(String[] args) {
        try {
            new TwitterProducer().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Twitter Client creation
    public Client createClient(BlockingQueue<String> blockingQueue) {
        var hosts = new HttpHosts(Constants.STREAM_HOST); // Twitter hosts
        var endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(terms); // Find the terms olympic, games, tokyo and 2020

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

    private void run() throws IOException {
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

        var schema = new Schema.Parser().parse(new File("schema/olympic_games_tokyo.avsc"));

        var recordInjection = GenericAvroCodecs.toBinary(schema);
        for (int i = 0; i < 100; i++) {
            String message = null;

            try {
                message = blockingQueue.poll(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                client.stop();
            }

            if (message != null) {
                var recordMetadata = new GenericData.Record(schema);
                recordMetadata.put("tweet_message", message);

                var bytes = recordInjection.apply(recordMetadata);
                var record = new ProducerRecord<String, byte[]>(KafkaConfig.TOPIC, bytes);
                producer.send(record);
                logger.info(record.toString());
            }
        }

        logger.info("\nApplication end");
    }
}