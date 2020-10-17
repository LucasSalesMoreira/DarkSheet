package com.sales.darksheet.ui.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sales.darksheet.R;
import java.util.ArrayList;

public class ChatsAdapter extends ArrayAdapter<ChatsModel> {

    private Context context;
    private int resourceXMLfile;
    private ArrayList<ChatsModel> arrayChats;

    public ChatsAdapter(Context context, int resourceXMLfile, ArrayList<ChatsModel> arrayChats) {
        super(context, resourceXMLfile, arrayChats);

        this.context = context;
        this.resourceXMLfile = resourceXMLfile;
        this.arrayChats = arrayChats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(resourceXMLfile, parent, false);

        ImageView imageView = (ImageView) row.findViewById(R.id.icon);
        TextView textName = (TextView) row.findViewById(R.id.nameChat);
        TextView textExtra = (TextView) row.findViewById(R.id.extra);

        ChatsModel chat = arrayChats.get(position);
        textName.setText(chat.getName());
        textExtra.setText(chat.getExtra());
        //imageView.setImageBitmap(chat.getImg());
        imageView.setImageResource(R.drawable.user_64);

        return row;
    }
}
