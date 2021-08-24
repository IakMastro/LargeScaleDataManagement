package avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.FsInput;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Deserializer {
    public static void main(String[] args) throws IOException {
        var configuration = new Configuration();
        var paths = new Path[2];
        paths[0] = new Path("hdfs://namenode:9000/Input/data_01.avro");
        paths[1] = new Path("hdfs://namenode:9000/Input/data_02.avro");

        var fileSystem = FileSystem.get(URI.create("hdfs://namenode:9000/data.txt"), configuration);
        var output = fileSystem.create(new Path("hdfs://namenode:9000/data.txt"));
        for (var path : paths) {
            var input = new FsInput(path, configuration);
            var schema = new Schema.Parser().parse(new File("schema/twitter.avsc"));
            var datumReader = new GenericDatumReader<GenericRecord>(schema);

            var fileReader = new DataFileReader<GenericRecord>(input, datumReader);
            GenericRecord twitterData = null;

            while (fileReader.hasNext()) {
                twitterData = fileReader.next();
                System.out.println(twitterData);
                output.writeChars(String.valueOf(twitterData));
            }
        }
        output.close();
    }
}
