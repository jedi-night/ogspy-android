package com.ogsteam.ogspy.data.models;

import com.ogsteam.ogspy.utils.OgspyUtils;

/**
 * Customer Account
 */
public class Account {
	
	private int id;
	private String username;
	private String password;
	private String serverUrl;
	private String serverUnivers;
	
	public Account(){		
	}
	
	public Account(String username, String password, String serverUrl, String serverUnivers){
		this.username=username;
		this.password=password;
		this.serverUrl=serverUrl;
		this.serverUnivers=serverUnivers;
	}
	
	public Account(int id, String username, String password, String serverUrl, String serverUnivers){
		this.id = id;
		this.username=username;
		this.password=password;
		this.serverUrl=serverUrl;
		this.serverUnivers=serverUnivers;
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

	public String getServerUnivers() {
		return serverUnivers;
	}

	public void setServerUnivers(String serverUnivers) {
		this.serverUnivers = serverUnivers;
	}

    @Override
    public String toString() {
        return (username + " - " + OgspyUtils.getUniversNameFromUrl(serverUnivers));
    }

}
