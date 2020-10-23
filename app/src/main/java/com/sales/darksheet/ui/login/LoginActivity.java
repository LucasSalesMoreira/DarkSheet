package com.sales.darksheet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.sales.darksheet.LoadActivity;
import com.sales.darksheet.R;
import com.sales.darksheet.connection.ConnectionIO;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private Socket socket = null;
    private String email = null, password = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText emailEditText = findViewById(R.id.username);
        EditText passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        socket = new ConnectionIO().connect();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                login();
            }
        });

        socket.on("login", emitterLogin);
    }

    private void login() {
        try {
            socket.emit("login", new JSONObject().put("email", email)
                    .put("password", password));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    Emitter.Listener emitterLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            JSONObject jsonObject = (JSONObject) args[0];
            System.out.println("Resposta do servidor: " + jsonObject);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadingProgressBar.setVisibility(View.GONE);
                    try {
                        if (jsonObject.getBoolean("ok") == true) {
                            socket.disconnect();
                            Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            showLoginFailed("Dados inválidos!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
}