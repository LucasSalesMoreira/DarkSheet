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

public class MessageAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<MessageModel> arrayList;

    public MessageAdapter(Context context, int resource, ArrayList arrayList) {
        super(context, resource, arrayList);

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(resource, parent, false);

        ImageView userPhoto = row.findViewById(R.id.user_photo);
        TextView name = row.findViewById(R.id.name);
        TextView msg = row.findViewById(R.id.msg);
        TextView dateTime = row.findViewById(R.id.date_and_time);

        MessageModel messageModel = arrayList.get(position);

        name.setText(messageModel.getName());
        msg.setText(messageModel.getMsg());
        dateTime.setText(messageModel.getDate());

        return row;
    }
}
