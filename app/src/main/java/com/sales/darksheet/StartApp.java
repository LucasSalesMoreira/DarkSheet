package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class StartApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);
        loadApp();
    }

    private void loadApp() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finishAffinity();
    }
}