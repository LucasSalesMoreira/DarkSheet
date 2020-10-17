package com.sales.darksheet.ui.chats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.sales.darksheet.R;
import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        ListView listView = root.findViewById(R.id.chats);

        ArrayList<ChatsModel> arrayChats = new ArrayList<>();
        arrayChats.add(new ChatsModel("Chat público", "Bem vindo!"));
        listView.setAdapter(new ChatsAdapter(getContext(), R.layout.simple_list_chats, arrayChats));

        return root;
    }
}
