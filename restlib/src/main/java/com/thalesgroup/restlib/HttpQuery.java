package com.thalesgroup.restlib;

/**
 * Public APIs
 */
public class HttpQuery {

    private IRestApiListener caller;
    private HttpQueryParameter searchParam;
    private IHttpQuery queryProvider;

    public HttpQuery(IRestApiListener caller, HttpQueryParameter searchParam) {
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
