package mapreduce.lang_count;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;

public class LangCountMapper extends MapReduceBase implements
        Mapper<AvroValue<GenericRecord>, Text, Text, IntWritable> {
    private static final Schema schema = new Schema.Parser().parse("schema/twitter.avsc");

    @Override
    public void map(AvroValue<GenericRecord> genericRecordAvroValue,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        var record = genericRecordAvroValue.datum();
        outputCollector.collect(new Text(record.get("lang").toString()), new IntWritable(1));
    }
}
