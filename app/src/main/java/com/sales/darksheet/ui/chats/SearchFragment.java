package com.sales.darksheet.ui.chats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sales.darksheet.base.Data;
import com.sales.darksheet.R;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.emitter.Emitter;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        Data.SOCKET.on("search_email", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                // Carregar ListView...
            }
        });

        EditText editEmail = view.findViewById(R.id.editEmail);
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEmail(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return view;
    }

    private void searchEmail(CharSequence s) {
        try {
            Data.SOCKET.emit("search_email", new JSONObject().put("byte", s));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}