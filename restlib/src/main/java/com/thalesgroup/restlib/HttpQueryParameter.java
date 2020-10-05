package com.thalesgroup.restlib;

/**
 * Parameters for rest-api query
 */
public class HttpQueryParameter {

    private String title;
    private String[] release;

    public HttpQueryParameter(String title, String[] release) {
        this.title = title;
        this.release = release;
    }

    public String getTitle() {
        return title;
    }

    public String[] getRelease() {
        return release;
    }
}
