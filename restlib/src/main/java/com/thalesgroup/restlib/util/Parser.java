package com.thalesgroup.restlib.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Util class for JSON parsing
 */
public class Parser {

    public static final String TMDB_RESULT_ROOT = "results";
    private static final String baseURL  = "https://image.tmdb.org/t/p/w500";


    /**
     * Parse content json object.
     *
     * @param inputStream the input stream
     * @return the json object
     * @throws IOException   the io exception
     * @throws JSONException the json exception
     */
    public static JSONObject ParseContent(InputStream inputStream)
            throws IOException, JSONException {

        // read the stream
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        // convert to string builder
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        // return as JSON object
        return new JSONObject(stringBuilder.toString());
    }

    /**
     * Gets title.
     *
     * @param jsonObject the json object
     * @return the title
     * @throws JSONException the json exception
     */
    public static String getTitle(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.getString("original_title");
    }


    /**
     * Gets release date.
     *
     * @param jsonObject the json object
     * @return the release
     * @throws JSONException the json exception
     */
    public static String getRelease(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.getString("release_date");
    }

    /**
     * Gets rating.
     *
     * @param jsonObject the json object
     * @return the rating
     * @throws JSONException the json exception
     */
    public static String getRating(JSONObject jsonObject)
            throws JSONException {
        return jsonObject.getString("vote_average");
    }

    /**
     * Gets poster url.
     *
     * @param jsonObject the json object
     * @return the poster
     * @throws JSONException the json exception
     */
    public static String getPoster(JSONObject jsonObject)
            throws JSONException {
        return baseURL + jsonObject.getString("poster_path");
    }


    /**
     * Add JSONObject to an ArrayList of JSONObject
     * @param jsonObject
     * @param jsonConcat
     * @throws JSONException
     */
    public static void buildJsonArrayList(JSONObject jsonObject, ArrayList<JSONObject> jsonConcat)
            throws JSONException {
        JSONArray jArr = jsonObject.getJSONArray(TMDB_RESULT_ROOT);
        for(int i=0; i<jArr.length(); i++) {
            jsonConcat.add(jArr.getJSONObject(i));
        }
    }

    /**
     * Add a root element and convert an array of JSONObject to JSONObject
     * @param jsonObjArr
     * @return
     */
    public static JSONObject addJsonRoot(JSONObject[] jsonObjArr) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray(Arrays.asList(jsonObjArr));
        try {
            jsonObject.put(TMDB_RESULT_ROOT, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
