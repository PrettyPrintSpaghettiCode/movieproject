package com.thalesgroup.restlib;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.thalesgroup.restlib.util.ApiKeyHandler;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.UnrecoverableKeyException;

import static com.thalesgroup.restlib.TestApiKey.API_KEY;
import static org.junit.Assert.*;

public class HttpQueryTest {

    private Instrumentation instrumentation  = InstrumentationRegistry.getInstrumentation();
    private Context context = instrumentation.getTargetContext();

    @Before
    public void setUp() throws Exception {
        try {
            if(ApiKeyHandler.retrieveAPIkey(context) != null) {
                return;
            }
        } catch (UnrecoverableKeyException | IOException e) {
            e.printStackTrace();
        }
        ApiKeyHandler.saveAPIKey(context, API_KEY);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void UnitTestNRM01() throws InterruptedException {
        try {
            instrumentation.runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    HttpQuery searchProvider = new HttpQuery(context,
                            new HttpQueryParameter("the", new String[]{"2020"}));
                    searchProvider.searchMovie();
                    instrumentation.waitForIdleSync();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void UnitTestNRM02() {
        HttpQuery searchProvider = new HttpQuery(context,
                new HttpQueryParameter("star", new String[]{"2017", "2018"}));
        searchProvider.searchMovie();
    }
}