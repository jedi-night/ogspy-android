package com.ogsteam.ogspy.data.models;

public class Account {
	
	private int id;
	private String username;
	private String password;
	private String serverUrl;
	
	public Account(){		
	}
	
	public Account(String username, String password, String serverUrl){
		this.username=username;
		this.password=password;
		this.serverUrl=serverUrl;
	}
	
	public Account(int id, String username, String password, String serverUrl){
		this.id = id;
		this.username=username;
		this.password=password;
		this.serverUrl=serverUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

}
