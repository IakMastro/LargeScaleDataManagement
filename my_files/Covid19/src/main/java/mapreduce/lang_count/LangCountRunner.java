package mapreduce.lang_count;

import org.apache.avro.mapred.AvroInputFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

public class LangCountRunner {
    public static void main(String[] args) {
        var conf = new JobConf(LangCountRunner.class);
        conf.setJobName("LangCount");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(LangCountMapper.class);
        conf.setReducerClass(LangCountReducer.class);

        conf.setInputFormat(AvroInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        FileInputFormat.setInputPaths(conf, new Path("hdfs://namenode:9000/Input/"));
        FileOutputFormat.setOutputPath(conf, new Path("hdfs://namenode:9000/LangCountResults"));

        try {
            JobClient.runJob(conf);
            System.out.println("Job was successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Something went wrong... Check logs");
        }
    }
}
