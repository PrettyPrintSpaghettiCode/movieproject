package com.thalesgroup.restlib;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

/**
 * Http library to perform rest-api calls to TMDb.
 */
public class TmdbRestApi extends AsyncTask<HttpQueryParameter, Void, JSONObject> {

    private static final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search/movie";
    protected static final String TMDB_RESULT_ROOT = "results";
    private static String TMDB_API_KEY = null;
    private IRestApiListener caller;

    public TmdbRestApi(IRestApiListener caller) {
        this.caller = caller;
        if(TMDB_API_KEY == null) {
            TMDB_API_KEY = ApiKeyHandler.retrieveAPIkey((Context)caller);
        }
    }

    @Override
    protected JSONObject doInBackground(HttpQueryParameter... httpQueryParameters) {
        URL oURL = null;
        HttpsURLConnection oConnection = null;
        JSONObject jsonObject = null;
        JSONArray jsonConcat = new JSONArray();

        HttpQueryParameter searchParam = httpQueryParameters[0];

        // encode the search parameter
        String query = null;
        try {
            query = URLEncoder.encode(searchParam.getTitle(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // send request for each release year as TMDb does not accept a range
        for (String year : searchParam.getRelease()) {
            try {
                // prepare rest-api query
                String url = TMDB_SEARCH_URL + "?api_key=" + TMDB_API_KEY +
                        "&primary_release_year=" + year +
                        "&language=en-US&query=" + query;

                // establish connection
                oURL = new URL(url);
                oConnection = (HttpsURLConnection) oURL.openConnection();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {

                if (oConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    // if connection is successful, parse the content
                    jsonObject = Parser.ParseContent(oConnection.getInputStream());
                    // concatenate the result for each request
                    concatJson(jsonObject, jsonConcat);

                } else {
                    Log.d("myLog", "Response code: " + oConnection.getResponseCode());
                    Log.d("myLog", "Response message: " + oConnection.getResponseMessage());
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                oConnection.disconnect();
            }
        }

        return convertJsonArrayToObject(jsonConcat);
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        caller.doOnPostExecute(jsonObject);
    }

    private void concatJson(JSONObject jsonObject, JSONArray jsonConcat)
            throws JSONException {

        JSONArray jArr = jsonObject.getJSONArray(TMDB_RESULT_ROOT);
        for(int i=0; i<jArr.length(); i++) {
            jsonConcat.put(jArr.getJSONObject(i));
        }
    }

    private JSONObject convertJsonArrayToObject(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TMDB_RESULT_ROOT, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
