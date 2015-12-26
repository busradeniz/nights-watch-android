package com.busradeniz.nightswatch.service.signup;

/**
 * Created by busradeniz on 27/12/15.
 */
public class Photo {
    private String fileName;
    private int id;
    private String url;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
