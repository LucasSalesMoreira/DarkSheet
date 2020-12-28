package com.sales.darksheet.ui.chats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.sales.darksheet.HomeActivity;
import com.sales.darksheet.R;
import com.sales.darksheet.base.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private String contact;
    private String emailContact;
    private ListView messagesView;
    private ArrayList<MessageModel> arrayMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Mostrar o botão adicional da TooBar (setinha)
        getSupportActionBar().setHomeButtonEnabled(true); // Tornando o botão adicional da TooBar clicável

        arrayMessages = new ArrayList<>();

        messagesView = findViewById(R.id.messages_view);
        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Mensagem número " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });

        Data.SOCKET.on("loadMessages", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONArray msgs = new JSONArray(String.valueOf(args[0]));
                    System.out.println(msgs);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (arrayMessages.toArray().length > 0)
                                arrayMessages.clear();

                            try {
                                for (int i = 0; i < msgs.length(); i++) {
                                    String email = msgs.getJSONObject(i).getString("sender_email");
                                    String name = null;

                                    if (email.equals(Data.EMAIL))
                                        name = Data.NAME;
                                    else
                                        name = contact;

                                    String message = msgs.getJSONObject(i).getString("text");
                                    String dateAndTime = msgs.getJSONObject(i).getString("date");

                                    arrayMessages.add(new MessageModel(message, name, dateAndTime));
                                }

                                messagesView.setAdapter(new MessageAdapter(getApplicationContext(), R.layout.simple_list_messages, arrayMessages));
                                messagesView.setSelection(arrayMessages.toArray().length);
                            } catch (JSONException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    });
                } catch (JSONException e) {
                    System.out.println(e.getMessage());
                }
            }
        });

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

        Data.SOCKET.off("NEW_MSG_IN_HOME"); //Cancelando para reescrever abaixo...

        Data.SOCKET.on("NEW_MSG_IN_CHAT", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject messageObject = new JSONObject(String.valueOf(args[0]));
                            System.out.println("Mensagem recebida: " + messageObject);
                            Data.SOCKET.emit("loadMessages", new JSONObject()
                                    .put("userEmail", Data.EMAIL).put("contactEmail", emailContact));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override // Botão BACK padrão do android
    public void onBackPressed() {
        Data.SOCKET.off("NEW_MSG_IN_CHAT");
        finishAffinity();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    @Override //Botão adicional na ToolBar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:  // ID do botão (gerado automaticamente pelo android)
                Data.SOCKET.off("NEW_MSG_IN_CHAT");
                finishAffinity();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    private void load() {
        try {
            int position = getIntent().getIntExtra("position", 0);
            contact = Data.CHATS.getJSONObject(position).getString("name");
            emailContact = Data.CHATS.getJSONObject(position).getString("email_contact");
            setTitle(contact);
            Data.SOCKET.emit("loadMessages", new JSONObject()
                    .put("userEmail", Data.EMAIL).put("contactEmail", emailContact));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String msg) {
        System.out.println(msg);
        JSONObject msgObject = new JSONObject();
        System.out.println("Email de usuário: " + Data.EMAIL);
        try {
            msgObject.put("emailUser", Data.EMAIL)
                    .put("emailContact", emailContact)
                    .put("contact", contact)
                    .put("text", msg);
            Data.SOCKET.emit("msg", msgObject);

            System.out.println(msgObject);

            Data.SOCKET.emit("loadMessages", new JSONObject()
                    .put("userEmail", Data.EMAIL).put("contactEmail", emailContact));
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }
}