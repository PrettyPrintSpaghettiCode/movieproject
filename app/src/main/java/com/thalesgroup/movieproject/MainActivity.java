package com.thalesgroup.movieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.thalesgroup.restlib.HttpQuery;
import com.thalesgroup.restlib.HttpQueryParameter;
import com.thalesgroup.restlib.IRestApiListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements IRestApiListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Call TMDb rest-api
     * @param view the view
     */
    public void onButtonSearchClick(View view) {
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        // get the title to search
        String searchTerm = editTextSearch.getText().toString();

        // call the public API of restlib to find movie
        HttpQuery searchProvider = new HttpQuery(this,
                new HttpQueryParameter(searchTerm, new String[]{"2017", "2018"}));
        searchProvider.searchMovie();
    }

    /**
     * Implement IRestApiListener method.
     * @param jsonObject result from AsyncTask
     */
    @Override
    public void doOnPostExecute(JSONObject jsonObject) {
        Intent intent = new Intent(this, ResultDisplayActivity.class);
        intent.putExtra("jsonObject", jsonObject.toString());
        this.startActivity(intent);
    }
}