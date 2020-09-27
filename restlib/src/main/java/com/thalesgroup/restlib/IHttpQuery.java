package com.thalesgroup.restlib;

public interface IHttpQuery {
    void findMovie(IRestApiListener caller, HttpQueryParameter httpQueryParameter);
    String getResultRoot();
}
