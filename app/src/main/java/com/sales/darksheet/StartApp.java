package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sales.darksheet.connection.ConnectionIO;
import com.sales.darksheet.fileManager.Manager;
import com.sales.darksheet.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class StartApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        Manager m = new Manager(getApplicationContext());
        String token = m.readToken();

        if (token.equals("XXX-XXX")) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finishAffinity();
        } else {
            Socket socket = new ConnectionIO().connect();

            socket.on("token_validation", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject response = new JSONObject(String.valueOf(args[0]));
                        socket.disconnect();
                        if (response.getBoolean("ok")) {
                            String email = response.getString("email");
                            String name = response.getString("name");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), LoadActivity.class)
                                            .putExtra("email", email).putExtra("name", name));
                                    finishAffinity();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finishAffinity();
                                }
                            });
                        }
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Falha ao logar na sessão!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finishAffinity();
                            }
                        });
                    }
                }
            });

            try {
                socket.emit("token_validation", new JSONObject().put("token", token));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}