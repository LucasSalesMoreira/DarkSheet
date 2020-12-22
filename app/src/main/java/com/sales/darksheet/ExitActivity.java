package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.sales.darksheet.fileManager.Manager;

public class ExitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        new Manager(getApplicationContext()).deleteToken();
        startActivity(new Intent(getApplicationContext(), StartApp.class));
        finishAffinity();
    }
}