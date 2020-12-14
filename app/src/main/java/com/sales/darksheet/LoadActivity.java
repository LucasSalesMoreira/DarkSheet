package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.sales.darksheet.base.Data;
import com.sales.darksheet.connection.ConnectionIO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoadActivity extends AppCompatActivity {

    private Socket socket = null;
    private String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        email = getIntent().getStringExtra("email");
        socket = new ConnectionIO().connect();
        loadApp();
    }

    private void loadApp() {
        Data.EMAIL = email;
        socket.on("loadContacts", loadContacts);
        socket.on("loadMessages", loadMessages);
        socket.emit("loadContacts", email);
    }

     Emitter.Listener loadContacts = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray chats = new JSONArray(String.valueOf(args[0]));
                System.out.println(chats);
                Data.CHATS = chats;
                socket.emit("loadMessages", email);
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    };

    Emitter.Listener loadMessages = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject msgs = new JSONObject(String.valueOf(args[0]));
                System.out.println(msgs.getString("email"));
                JSONArray inbox = msgs.getJSONArray("inbox");
                JSONArray sendbox = msgs.getJSONArray("sendbox");
                System.out.println(inbox);
                System.out.println(sendbox);
                Data.MESSAGES = msgs;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Data.SOCKET = socket;
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finishAffinity();
                    }
                });
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }
    };
}