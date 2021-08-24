package mapreduce.verified;

import org.apache.avro.mapred.AvroInputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class VerifiedRunner {
    public static void main(String[] args) {
        var conf = new JobConf(VerifiedRunner.class);
        conf.setJobName("Verified");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(VerifiedMapper.class);
        conf.setReducerClass(VerifiedReducer.class);

        conf.setInputFormat(AvroInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path("hdfs://namenode:9000/Input/"));
        FileOutputFormat.setOutputPath(conf, new Path("hdfs://namenode:9000/VerifiedResults"));

        try {
            JobClient.runJob(conf);
            System.out.println("Job was successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Something went wrong... Check logs");
        }
    }
}
