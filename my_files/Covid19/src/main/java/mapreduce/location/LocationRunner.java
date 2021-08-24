package mapreduce.location;

import org.apache.avro.mapred.AvroInputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class LocationRunner {
    public static void main(String[] args) {
        var conf = new JobConf(LocationRunner.class);
        conf.setJobName("LocationRunner");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(LocationMapper.class);
        conf.setReducerClass(LocationReducer.class);

        conf.setInputFormat(AvroInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path("hdfs://namenode:9000/Input/"));
        FileOutputFormat.setOutputPath(conf, new Path("hdfs://namenode:9000/LocationResults"));

        try {
            JobClient.runJob(conf);
            System.out.println("Job was successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Something went wrong... Check logs");
        }
    }
}
