package com.sales.darksheet.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sales.darksheet.R;
import com.sales.darksheet.conf.Conf;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity {

    private Socket socket = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                try {
                    socket.emit("login", new JSONObject()
                            .put("email", "teste@gmail.com")
                            .put("password", "12345678"));
                } catch (JSONException e) {
                    Log.d("ERROR_JSON", e.getMessage());
                }
            }
        });

        try {
            socket = IO.socket(Conf.SERVER_HOST);
            socket.connect();
        } catch (URISyntaxException e) {
            Log.d("ERROR_CONNECTION", e.getMessage());
        }


        socket.on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = (JSONObject) args[0];
                        System.out.println("Resposta do servidor: " + jsonObject);
                    }
                });
            }
        });

    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}