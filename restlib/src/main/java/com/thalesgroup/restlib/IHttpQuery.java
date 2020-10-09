package com.thalesgroup.restlib;

import android.content.Context;

/**
 * Interface to be implemented by rest-api query provider
 */
public interface IHttpQuery {
    void findMovie(Context caller, HttpQueryParameter httpQueryParameter);
    String getResultRoot();
}
