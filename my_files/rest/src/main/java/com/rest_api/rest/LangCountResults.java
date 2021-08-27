package com.rest_api.rest;

public class LangCountResults {
    private String lang;
    private String count;

    public LangCountResults(String lang, String count) {
        this.lang = lang;
        this.count = count;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
