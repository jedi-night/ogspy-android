package com.ogsteam.ogspy.data.models;

public class Message {

	private int id;
    private String datetime;
	private String sender;
	private String content;

	public Message(){
	}

	public Message(String datetime, String sender, String content){
		this.sender=sender;
		this.content=content;
		this.datetime=datetime;
	}

	public Message(int id, String datetime, String sender, String content){
		this.id = id;
        this.datetime=datetime;
        this.sender=sender;
        this.content=content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
