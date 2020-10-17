package com.sales.darksheet.ui.chats;

import android.graphics.Bitmap;

public class ChatsModel {
    private String name;
    private String extra;
    private Bitmap img;

    public ChatsModel(String name, String extra, Bitmap img) {
        this.name = name;
        this.extra = extra;
        this.img = img;
    }

    public ChatsModel(String name, String extra) {
        this.name = name;
        this.extra = extra;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
