package LowestTemperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

public class LowestTemperatureReducer extends MapReduceBase
        implements Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    public void reduce(Text text,
                       Iterator<IntWritable> iterator,
                       OutputCollector<Text, IntWritable> outputCollector,
                       Reporter reporter) throws IOException {
        var sum = 0;

        while (iterator.hasNext()) {
            if (LowestTemperatureMapper.minTempList.contains(Double.parseDouble(String.valueOf(text)))) {
                sum += iterator.next().get();
                outputCollector.collect(text, new IntWritable(sum));
            }

            else
                iterator.next();
        }
    }
}
