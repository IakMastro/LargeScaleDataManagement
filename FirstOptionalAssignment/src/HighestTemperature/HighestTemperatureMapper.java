package HighestTemperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.ArrayList;

public class HighestTemperatureMapper extends MapReduceBase
        implements Mapper<LongWritable, Text, Text, IntWritable> {
    public static final ArrayList<Double> maxTempList = new ArrayList<Double>();

    public HighestTemperatureMapper() {
        for (int i = 0; i < 5; i++)
            maxTempList.add(Double.MIN_VALUE);
    }

    @Override
    public void map(LongWritable longWritable,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        var line = text.toString().split(",")[1].trim();

        if (!line.equals("MaxTemp")) {
            var currentSong = Double.parseDouble(line);
            for (int i = 0; i < 5; i++) {
                if (currentSong > maxTempList.get(i)) {
                    maxTempList.add(i, currentSong);
                    maxTempList.remove(5);
                    outputCollector.collect(new Text(String.valueOf(currentSong)), new IntWritable(1));
                    break;
                }

                if (currentSong == maxTempList.get(i)) {
                    outputCollector.collect(new Text(String.valueOf(currentSong)), new IntWritable(1));
                    break;
                }
            }
        }
    }
}
