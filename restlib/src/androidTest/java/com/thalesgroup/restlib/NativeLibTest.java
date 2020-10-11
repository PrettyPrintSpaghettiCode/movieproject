package com.thalesgroup.restlib;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.thalesgroup.restlib.TmdbRestApi;


public class NativeLibTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void NativeUnitTestNRM01() {
        JSONObject[] array = new JSONObject[6];
        float[] expected_rating = {6.9f, 6.7f, 6.7f, 5.7f, 4.6f, 0.0f};
        float rating;

        // prepare test data
        try {
            array[0] = new JSONObject();
            array[0].put("title", "Star Wars: The Last Jedi");
            array[0].put("vote_average", "6.9");
            array[1] = new JSONObject();
            array[1].put("title", "Frat Star");
            array[1].put("vote_average", "4.6");
            array[2] = new JSONObject();
            array[2].put("title","The Star");
            array[2].put("vote_average", "5.7");
            array[3] = new JSONObject();
            array[3].put("title","Pup Star: Better 2Gether");
            array[3].put("vote_average", "6.7");
            array[4] = new JSONObject();
            array[4].put("title","Star of the Sea");
            array[4].put("vote_average", "0");
            array[5] = new JSONObject();
            array[5].put("title","Billy Star");
            array[5].put("vote_average", "6.7");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // call the native library to sort by rating
        TmdbRestApi.sortArray(array);

        // verify sorted rating
        try {
            for (int i = 0; i < array.length; i++) {
                rating = Float.parseFloat(array[i].getString("vote_average"));
                Assert.assertEquals(expected_rating[i],rating, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}