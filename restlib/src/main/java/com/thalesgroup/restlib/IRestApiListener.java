package com.thalesgroup.restlib;

import org.json.JSONObject;


/**
 * Interface to be implemented by caller app.
 */
public interface IRestApiListener {
    public void doOnPostExecute(JSONObject jsonObject);
}