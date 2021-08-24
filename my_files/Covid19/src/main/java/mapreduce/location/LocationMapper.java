package mapreduce.location;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroWrapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class LocationMapper extends MapReduceBase implements
        Mapper<AvroWrapper<GenericRecord>, NullWritable, Text, IntWritable> {
    @Override
    public void map(
            AvroWrapper<GenericRecord> genericRecordAvroWrapper,
            NullWritable nullWritable,
            OutputCollector<Text, IntWritable> outputCollector,
            Reporter reporter
    ) throws IOException {
        var location = genericRecordAvroWrapper.datum().get("location").toString();
        outputCollector.collect(
                new Text(!location.equals("null") ? location : "undefined"),
                new IntWritable(1)
        );
    }
}
