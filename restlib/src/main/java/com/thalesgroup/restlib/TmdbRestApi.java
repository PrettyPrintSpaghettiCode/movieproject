package com.thalesgroup.restlib;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.thalesgroup.restlib.util.ApiKeyHandler;
import com.thalesgroup.restlib.util.Parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Http library to perform rest-api calls to TMDb.
 */
public class TmdbRestApi extends AsyncTask<HttpQueryParameter, Void, JSONObject> {

    private static final String TMDB_SEARCH_URL = "https://api.themoviedb.org/3/search/movie";
    private static String TMDB_API_KEY = null;
    private Context caller;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("sort-lib");
    }

    public TmdbRestApi(Context caller) {
        this.caller = caller;
        if(TMDB_API_KEY == null) {
            try {
                TMDB_API_KEY = ApiKeyHandler.retrieveAPIkey(caller);
            } catch (UnrecoverableKeyException | IOException e) {
                e.printStackTrace();
                Toast.makeText(caller, "API key not set!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected JSONObject doInBackground(HttpQueryParameter... httpQueryParameters) {
        URL oURL = null;
        HttpsURLConnection oConnection = null;
        JSONObject jsonObject = null;
        ArrayList<JSONObject> jsonConcat = new ArrayList<JSONObject>();

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
                    Parser.buildJsonArrayList(jsonObject, jsonConcat);
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

        //sorting by native C lib
        JSONObject[] array = jsonConcat.toArray(new JSONObject[jsonConcat.size()]);
        sortArray(array);

        // add root "results" and return as JSONObject
        return Parser.addJsonRoot(array);
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);
        if (caller instanceof IRestApiListener) {
            ((IRestApiListener) caller).doOnPostExecute(json);
        }
    }

    /**
     *  Native library to sort rating in descending order
     */
    private native void sortArray(JSONObject[] json);

}
