package LowestTemperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.ArrayList;

public class LowestTemperatureMapper extends MapReduceBase
        implements Mapper<LongWritable, Text, Text, IntWritable> {
    public static final ArrayList<Double> minTempList = new ArrayList<Double>();

    public LowestTemperatureMapper() {
        for (int i = 0; i < 10; i++)
            minTempList.add(Double.MAX_VALUE);
    }

    @Override
    public void map(LongWritable longWritable,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        var line = text.toString().split(",")[0].trim();

        if (!line.equals("MinTemp")) {
            var currentSong = Double.parseDouble(line);

            for (int i = 0; i < 10; i++) {
                if (currentSong < minTempList.get(i)) {
                    minTempList.add(i, currentSong);
                    minTempList.remove(10);
                    outputCollector.collect(new Text(String.valueOf(currentSong)), new IntWritable(1));
                    break;
                }

                if (currentSong == minTempList.get(i)) {
                    outputCollector.collect(new Text(String.valueOf(currentSong)), new IntWritable(1));
                    break;
                }
            }
        }
    }
}
