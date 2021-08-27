package com.rest_api.rest;

public class VerifiedResults {
    private String verified;
    private String percentage;

    public VerifiedResults(String verified, String percentage) {
        this.verified = verified;
        this.percentage = percentage;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
