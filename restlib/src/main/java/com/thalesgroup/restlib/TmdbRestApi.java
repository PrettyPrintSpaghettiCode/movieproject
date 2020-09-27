package com.thalesgroup.restlib;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

/**
 * Http library to perform rest-api calls to TMDb.
 */
public class TmdbRestApi extends AsyncTask<String, Void, JSONObject> {

    private IRestApiListener caller;

    public TmdbRestApi(IRestApiListener caller) {
        this.caller = caller;
    }

    @Override
    protected JSONObject doInBackground(String... searchParams) {

        URL oURL = null;
        HttpsURLConnection oConnection = null;
        JSONObject jsonObject = null;

        try {
            // prepare rest query
            String query = URLEncoder.encode(searchParams[0],"UTF-8");
            String url = "https://api.themoviedb.org/3/search/movie?api_key=4d6ee52c4891f4013dbdcad73a75f1c7&primary_release_year=2017&language=en-US&query=" + query;

            // establish connection
            oURL = new URL(url);
            oConnection = (HttpsURLConnection) oURL.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {

            if(oConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                // if connection is successful, parse the content
                jsonObject = Parser.ParseContent(oConnection.getInputStream());
            } else {
                Log.d("myLog", "Response code: " + oConnection.getResponseCode());
                Log.d("myLog", "Response message: " + oConnection.getResponseMessage());
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            oConnection.disconnect();
        }

        return jsonObject;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        caller.doOnPostExecute(jsonObject);
    }
}
