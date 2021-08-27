package com.rest_api.rest;

public class FilterResults {
    private String filter_level;
    private String count;

    public FilterResults(String filter_level, String count) {
        this.filter_level = filter_level;
        this.count = count;
    }

    public String getFilter_level() {
        return filter_level;
    }

    public void setFilter_level(String filter_level) {
        this.filter_level = filter_level;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
