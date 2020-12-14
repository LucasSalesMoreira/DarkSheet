package com.sales.darksheet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import com.sales.darksheet.base.Data;
import com.sales.darksheet.connection.ConnectionIO;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.Socket;

public class ChatActivity extends AppCompatActivity {

    //private Socket socket = null;
    private String contact = null;
    private String emailContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //socket = new ConnectionIO().connect();
        load();
        EditText editText = findViewById(R.id.msg);
        ImageButton btSend = findViewById(R.id.btSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(editText.getText().toString());
                editText.setText(null);
            }
        });
    }

    private void load() {
        try {
            int position = getIntent().getIntExtra("position", 0);
            contact = Data.CHATS.getJSONObject(position).getString("name");
            emailContact = Data.CHATS.getJSONObject(position).getString("email_contact");
            setTitle(contact);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendMessage(String msg) {
        System.out.println(msg);
        JSONObject msgObject = new JSONObject();
        System.out.println("Email de usuário: " + Data.EMAIL);
        try {
            msgObject.put("email", Data.EMAIL)
                    .put("emailContact", emailContact)
                    .put("contact", contact)
                    .put("text", msg)
                    .put("date", null)
                    .put("type", "out");
            Data.SOCKET.emit("msg", msgObject);
            System.out.println(msgObject);
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }
}