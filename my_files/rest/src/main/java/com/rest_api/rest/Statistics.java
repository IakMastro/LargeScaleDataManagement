package com.rest_api.rest;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Statistics {
    private ArrayList<FilterResults> filters;
    private ArrayList<LangCountResults> languages;
    private ArrayList<LocationResults> locations;
    private ArrayList<VerifiedResults> verified;

    public Statistics(Configuration conf) throws IOException, URISyntaxException {
        var url = "hdfs://namenode:9000/";
        filters = new ArrayList<>();
        languages = new ArrayList<>();
        locations = new ArrayList<>();
        verified = new ArrayList<>();

        for (var line : getDataFromFiles(conf, new Path(url.concat("FilterResults/"))).split("\n")) {
            var data = line.split("\t");
            if (data.length >= 2) {
                filters.add(new FilterResults(data[0], data[1]));
            }
        }

        for (var line : getDataFromFiles(conf, new Path(url.concat("LangCountResults/"))).split("\n")) {
            var data = line.split("\t");
            if (data.length >= 2) {
                languages.add(new LangCountResults(data[0], data[1]));
            }
        }

        for (var line : getDataFromFiles(conf, new Path(url.concat("LocationResults/"))).split("\n")) {
            var data = line.split("\t");
            if (data.length >= 2) {
                locations.add(new LocationResults(data[0], data[1]));
            }
        }

        for (var line : getDataFromFiles(conf, new Path(url.concat("VerifiedResults/"))).split("\n")) {
            var data = line.split("\t");
            if (data.length >= 2) {
                verified.add(new VerifiedResults(data[0], data[1]));
            }
        }
    }

    private String getDataFromFiles(Configuration conf, Path path) throws URISyntaxException, IOException {
        var fileSystem = FileSystem.get(new URI("hdfs://namenode:9000"), conf);
        var statuses = fileSystem.listStatus(new Path(String.valueOf(path)));

        String content = null;
        for (var status : statuses) {
            if (!status.equals("_SUCCESS")) {
                var inputStream = fileSystem.open(status.getPath());
                content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            }
        }

        return content;
    }

    public ArrayList<FilterResults> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<FilterResults> filters) {
        this.filters = filters;
    }

    public ArrayList<LangCountResults> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<LangCountResults> languages) {
        this.languages = languages;
    }

    public ArrayList<LocationResults> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<LocationResults> locations) {
        this.locations = locations;
    }

    public ArrayList<VerifiedResults> getVerified() {
        return verified;
    }

    public void setVerified(ArrayList<VerifiedResults> verified) {
        this.verified = verified;
    }
}
