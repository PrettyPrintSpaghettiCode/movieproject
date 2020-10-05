package com.thalesgroup.restlib;

/**
 * Interface to be implemented by rest-api query provider
 */
public interface IHttpQuery {
    void findMovie(IRestApiListener caller, HttpQueryParameter httpQueryParameter);
    String getResultRoot();
}
