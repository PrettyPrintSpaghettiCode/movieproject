package com.thalesgroup.movieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thalesgroup.restlib.ApiKeyHandler;


public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //TODO: should retrieve API key here and
        // display a notification if API key has not been set yet
    }

    public void onButtonSetAPIKeyClick(View view) {
        EditText editTextAPIKey = findViewById(R.id.editTextAPIKey);
        // store the new API key securely
        ApiKeyHandler.saveAPIKey(this, editTextAPIKey.getText().toString());
        Toast.makeText(this, "Saved...", Toast.LENGTH_LONG).show();
    }

}