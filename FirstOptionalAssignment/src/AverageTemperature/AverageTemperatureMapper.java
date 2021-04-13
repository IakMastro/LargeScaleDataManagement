package AverageTemperature;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class AverageTemperatureMapper extends MapReduceBase
        implements Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    public void map(LongWritable longWritable,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        var minTempLabel = text.toString().split(",")[0].trim();

        if (!minTempLabel.equals("MinTemp")) {
            var minTemp = Double.parseDouble(minTempLabel);
            var maxTemp = Double.parseDouble(text.toString().split(",")[1].trim());

            var avgTemp = (maxTemp + minTemp) / 2;

            if (avgTemp > 10 && avgTemp < 20)
                outputCollector.collect(new Text("A"), new IntWritable(1));

            else if (avgTemp > 21 && avgTemp < 30)
                outputCollector.collect(new Text("B"), new IntWritable(1));

            else if (avgTemp > 31 && avgTemp < 40)
                outputCollector.collect(new Text("C"), new IntWritable(1));
        }
    }
}
