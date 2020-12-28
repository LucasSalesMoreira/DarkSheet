package com.sales.darksheet.ui.chats;

public class MessageModel {
    private String msg;
    private String name;
    private String date;

    public MessageModel(String msg, String name, String date) {
        this.msg = msg;
        this.name = name;
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
