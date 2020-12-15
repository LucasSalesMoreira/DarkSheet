package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.sales.darksheet.fileManager.Manager;
import com.sales.darksheet.ui.login.LoginActivity;

public class StartApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        Manager m = new Manager(getApplicationContext());

        if (m.readToken().equals(null)) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finishAffinity();
        } else {

            //validar token com a API...

            startActivity(new Intent(getApplicationContext(), LoadActivity.class));
            finishAffinity();
        }

    }
}