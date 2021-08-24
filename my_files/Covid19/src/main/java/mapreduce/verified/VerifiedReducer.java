package mapreduce.verified;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Iterator;

public class VerifiedReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, Text> {
    @Override
    public void reduce(
            Text text,
            Iterator<IntWritable> iterator,
            OutputCollector<Text, Text> outputCollector,
            Reporter reporter
    ) throws IOException {
        var sum = 0.0;

        while (iterator.hasNext())
            sum += iterator.next().get();

        var average = (sum / VerifiedMapper.totalTweets) * 100;
        outputCollector.collect(text, new Text(String.valueOf(average).concat("%")));
    }
}
