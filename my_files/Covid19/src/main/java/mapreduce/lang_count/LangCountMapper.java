package mapreduce.lang_count;

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

public class LangCountMapper extends MapReduceBase implements
        Mapper<AvroWrapper<GenericRecord>, NullWritable, Text, IntWritable> {
    @Override
    public void map(AvroWrapper<GenericRecord> genericRecordAvroWrapper,
                    NullWritable text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        var record = genericRecordAvroWrapper.datum();
        outputCollector.collect(new Text(record.get("lang").toString()), new IntWritable(1));
    }
}
