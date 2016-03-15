package com.ogsteam.ogspy.fragments.tabs.items;

/**
 * Created by jp.tessier on 18/06/13.
 */
public class MessageItem {
    private String date;
    private String sender;
    private String message;

    public MessageItem(String date, String sender, String message){
        this.date = date;
        this.sender = sender;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
