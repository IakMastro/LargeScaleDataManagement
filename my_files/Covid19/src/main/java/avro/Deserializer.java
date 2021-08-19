package avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;

public class Deserializer {
    public static void main(String[] args) throws IOException {
        var configuration = new Configuration();
        var input = new FsInput(new Path("hdfs://namenode:9000/data_01.avro"), configuration);

        var schema = new Schema.Parser().parse(new File("schema/twitter.avsc"));
        var datumReader = new GenericDatumReader<GenericRecord>(schema);

        var fileReader = new DataFileReader<GenericRecord>(input, datumReader);
        GenericRecord twitterData = null;

        while (fileReader.hasNext()) {
            twitterData = fileReader.next();
            System.out.println(twitterData);
        }
    }
}
