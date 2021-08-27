package com.rest_api.rest;

public class Tweet {
    private String name;
    private String location;
    private String text;
    private String lang;
    private String filter;

    public Tweet(String name, String location, String text, String lang, String filter) {
        this.name = name;
        this.location = location;
        this.text = text;
        this.lang = lang;
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
