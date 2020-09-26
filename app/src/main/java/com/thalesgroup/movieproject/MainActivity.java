package com.thalesgroup.movieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.thalesgroup.restlib.TmdbRestApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * call TMDB rest-api
     *
     * @param view the view
     */
    public void onButtonSearchClick(View view) {
        EditText editTextSearch = findViewById(R.id.editTextSearch);
        String searchTerm = editTextSearch.getText().toString();

        // pass searchTerm to rest-api
        new TmdbRestApi().execute(searchTerm);
    }
}