package com.thalesgroup.movieproject;

public class ResultDisplay {

    private final String title;
    private final String release;
    private final String rating;

    public ResultDisplay(String title, String release, String rating) {
        this.title = title;
        this.release = release;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease() {
        return release;
    }

    public String getRating() {
        return rating;
    }
}
