package com.sales.darksheet.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sales.darksheet.R;
import com.sales.darksheet.base.Data;
import org.json.JSONArray;
import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    private ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        listView = root.findViewById(R.id.chats);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        ArrayList<ChatsModel> arrayList = loadView(Data.CHATS);
        listView.setAdapter(new ChatsAdapter(getContext(), R.layout.simple_list_chats, arrayList));

        return root;
    }

    private ArrayList<ChatsModel> loadView(JSONArray chats) {
        ArrayList<ChatsModel> arrayChats = new ArrayList<>();
        for (int i = 0; i < chats.length(); i++) {
            try {
                arrayChats.add(new ChatsModel(chats.getJSONObject(i).getString("name"),
                        chats.getJSONObject(i).getString("email_contact")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return arrayChats;
    }
}
