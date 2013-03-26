package com.ogsteam.ogspy.data.models;

public class Prefs {
	
	private int id;
	private int refreshHostiles;

	public Prefs(){		
	}
	
	public Prefs(int refreshHostiles){
		this.refreshHostiles=refreshHostiles;
	}
	
	public Prefs(int id, int refreshHostiles){
		this.id = id;
		this.refreshHostiles=refreshHostiles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRefreshHostiles() {
		return refreshHostiles;
	}

	public void setRefreshHostiles(int refreshHostiles) {
		this.refreshHostiles = refreshHostiles;
	}

}
