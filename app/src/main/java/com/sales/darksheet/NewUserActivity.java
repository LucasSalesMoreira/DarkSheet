package com.sales.darksheet;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sales.darksheet.base.Data;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class NewUserActivity extends AppCompatActivity {

    private EditText txtPass1, txtPass2, txtEmail, txtName;
    private String email, name, pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtNick);
        txtPass1 = findViewById(R.id.txtPass1);
        txtPass2 = findViewById(R.id.txtPass2);

        Button btCreateNewUser = findViewById(R.id.btCreateNewUser);
        btCreateNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidCredentials())
                    sendAuthentication();
                else
                    Toast.makeText(getApplicationContext(), "Dados inválidos!", Toast.LENGTH_SHORT).show();
            }
        });

        Data.SOCKET.on("new_authentication", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(String.valueOf(args[0]));
                            if (result.getBoolean("ok")) {
                                Toast.makeText(getApplicationContext(), "Código de verificação enviado!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), CodeValidationActivity.class)
                                        .putExtra("pass", pass1).putExtra("email", email));
                                finishAffinity();
                            } else {
                                Toast.makeText(getApplicationContext(), "O email já existe!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private boolean isValidCredentials() {

        email = txtEmail.getText().toString();
        name = txtName.getText().toString();
        pass1 = txtPass1.getText().toString();
        pass2 = txtPass2.getText().toString();

        if (!email.equals(null) && !email.isEmpty() && !name.equals(null) && !name.isEmpty()
                && !pass1.equals(null) && !pass1.isEmpty() && !pass2.equals(null) && !pass2.isEmpty()) {
            if (pass1.equals(pass2)) {
                return true;
            }
        }

        return false;
    }

    private void sendAuthentication() {
        try {
            JSONObject object = new JSONObject();
            object.put("name", name).put("email", email);
            Data.SOCKET.emit("new_authentication", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}