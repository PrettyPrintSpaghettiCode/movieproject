package com.thalesgroup.restlib;

import android.content.Context;

/**
 * Public APIs
 */
public class HttpQuery {

    private Context caller;
    private HttpQueryParameter searchParam;
    private IHttpQuery queryProvider;

    public HttpQuery(Context caller, HttpQueryParameter searchParam) {
        this.caller = caller;
        this.searchParam = searchParam;
        this.queryProvider = new TmdbHttpQuery();
    }

    public void searchMovie() {
        queryProvider.findMovie(this.caller, this.searchParam);
    }

    public String getRootName() {
        return queryProvider.getResultRoot();
    }
}
