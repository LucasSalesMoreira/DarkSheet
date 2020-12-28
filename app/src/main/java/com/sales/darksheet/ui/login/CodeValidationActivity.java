package com.sales.darksheet.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sales.darksheet.R;
import com.sales.darksheet.base.Data;
import com.sales.darksheet.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class CodeValidationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_validation);

        EditText txtCode = findViewById(R.id.txtCode);
        ProgressBar progressBar = findViewById(R.id.bar);
        Button btSendCode = findViewById(R.id.btSend);
        btSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    String code = txtCode.getText().toString();
                    String password = getIntent().getStringExtra("pass");
                    String email = getIntent().getStringExtra("email");
                    Data.SOCKET.emit("authenticating", new JSONObject()
                            .put("code", code)
                            .put("email", email)
                            .put("password", password));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Data.SOCKET.on("authenticating", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject response = new JSONObject(String.valueOf(args[0]));
                            System.out.println("Response: " + response);
                            if (response.getBoolean("ok")) {
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finishAffinity();
                            } else {
                                Toast.makeText(getApplicationContext(), "Falha na autenticação!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Falha crítica no app!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}