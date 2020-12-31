package com.sales.darksheet.ui.chats;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sales.darksheet.base.Data;
import com.sales.darksheet.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import io.socket.emitter.Emitter;

public class SearchFragment extends Fragment {

    ArrayList<ChatsModel> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ListView listView = view.findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendEmailSelected(arrayList.get(position).getExtra());
            }
        });

        Data.SOCKET.on("search_email", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                arrayList = loadView(new JSONArray(String.valueOf(args[0])));
                                listView.setAdapter(new ChatsAdapter(getContext(), R.layout.simple_list_chats, arrayList));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    System.out.println("activity = " + activity);
                }
            }
        });

        Data.SOCKET.on("add_contact", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject response = new JSONObject(String.valueOf(args[0]));
                                if (response.getBoolean("ok")) {
                                    Toast.makeText(getContext(), "Adicionado aos contatos", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    System.out.println("activity = " + activity);
                }
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
            Data.SOCKET.emit("search_email", new JSONObject().put("byte", s)
                    .put("emailUser", Data.EMAIL));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendEmailSelected(String emailContact) {
        try {
            Data.SOCKET.emit("add_contact", new JSONObject()
                    .put("emailUser", Data.EMAIL)
                    .put("emailContact", emailContact));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ChatsModel> loadView(JSONArray chats) {
        ArrayList<ChatsModel> arrayChats = new ArrayList<>();
        for (int i = 0; i < chats.length(); i++) {
            try {
                arrayChats.add(new ChatsModel(chats.getJSONObject(i).getString("name"),
                        chats.getJSONObject(i).getString("email")));
                System.out.println(chats);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return arrayChats;
    }
}