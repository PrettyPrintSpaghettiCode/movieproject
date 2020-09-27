package com.thalesgroup.movieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.thalesgroup.restlib.Parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ResultDisplay> listResult;

    private final int MAX_RESULT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_display);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        JSONArray jsonArray = parseIntentArgs(getIntent());
        List<ResultDisplay>  listContent = getContentList(jsonArray);

        adapter = new RecyclerViewAdapter(listContent, this);
        recyclerView.setAdapter(adapter);

    }

    /**'
     * Format intent args into JSON array
     * @param intent for retrieving intent args
     * @return JSONArray results
     */
    private JSONArray parseIntentArgs(Intent intent) {
        JSONObject json = null;
        try {
            json = new JSONObject(intent.getStringExtra("jsonObject"));
            return json.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Format JSONArray into ResultDisplay list to be displayed in UI
     * @param jsonArray
     * @return ResultDisplay list
     */
    private List<ResultDisplay> getContentList(JSONArray jsonArray) {
        JSONObject jsonObject = null;
        ResultDisplay result = null;
        listResult = new ArrayList<>();

        //TODO: sorting by native C lib

        for(int i=0; i<jsonArray.length(); i++) {
            // break loop once exceeds MAX_RESULT
            if(i >= MAX_RESULT) break;

            jsonObject = null;
            result = null;
            try {
                jsonObject = jsonArray.getJSONObject(i);
                result = new ResultDisplay(Parser.getTitle(jsonObject),
                        Parser.getRelease(jsonObject),
                        Parser.getRating(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listResult.add(result);
        }

        return listResult;
    }
}