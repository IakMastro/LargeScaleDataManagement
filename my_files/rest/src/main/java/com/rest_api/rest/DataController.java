package com.rest_api.rest;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

@RestController
public class DataController {
    private ArrayList<Tweet> tweets;
    private Statistics statistics;

    public DataController() throws IOException, URISyntaxException {
        var configuration = new Configuration();
        var paths = new Path[2];
        tweets = new ArrayList<>();
        paths[0] = new Path("hdfs://namenode:9000/Input/data_01.avro");
        paths[1] = new Path("hdfs://namenode:9000/Input/data_02.avro");

        for (var path : paths) {
            var input = new FsInput(path, configuration);
            var schema = new Schema.Parser().parse(new File("schema/twitter.avsc"));
            var datumReader = new GenericDatumReader<GenericRecord>(schema);

            var fileReader = new DataFileReader<GenericRecord>(input, datumReader);
            GenericRecord twitterData = null;
            while (fileReader.hasNext()) {
                twitterData = fileReader.next();
                tweets.add(new Tweet(
                        twitterData.get("name").toString(),
                        twitterData.get("location").toString(),
                        twitterData.get("text").toString(),
                        twitterData.get("lang").toString(),
                        twitterData.get("filter").toString()
                ));
            }
        }

        statistics = new Statistics(configuration);
    }

    @GetMapping("/data")
    public ArrayList<Tweet> getData() {
        return tweets;
    }

    @GetMapping("/")
    public Statistics getStatistics() {
        return statistics;
    }
}
