package HighestTemperature;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Locale;

public class HighestTemperatureRunner {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        var conf = new JobConf(HighestTemperatureRunner.class);
        conf.setJobName("LastFiveHighestTemperatures");

        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(HighestTemperatureMapper.class);
        conf.setCombinerClass(HighestTemperatureReducer.class);
        conf.setReducerClass(HighestTemperatureReducer.class);

        conf.setInputFormat(TextInputFormat.class);
        conf.setOutputFormat(TextOutputFormat.class);

        var paths = new Path[2];
        paths[0] = new Path(args[0]);
        paths[1] = new Path(args[1]);

        FileInputFormat.setInputPaths(conf, paths);
        FileOutputFormat.setOutputPath(conf, new Path(args[2]));

        try {
            JobClient.runJob(conf);
            System.out.println("Job was successful");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Job failed...");
        }
    }
}
