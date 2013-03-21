package com.ogsteam.ogspy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "ogspy";
 
    // Acounts table name
    protected static final String TABLE_ACCOUNTS = "accounts";
 
    // Account Table Columns names
    protected static final String KEY_ID = "id";
    protected static final String KEY_USERNAME = "username";
    protected static final String KEY_PASSWORD = "password";
    protected static final String KEY_SERVER_URL = "server_url";
    protected static final String KEY_SERVER_UNIVERS = "server_univers";    
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_USERNAME + " TEXT,"
                + KEY_PASSWORD + " TEXT," 
                + KEY_SERVER_URL + " TEXT,"
                + KEY_SERVER_UNIVERS + " TEXT"
        		+ ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		 if(oldVersion == 1){
			 db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD "+ KEY_SERVER_UNIVERS + " TEXT");
		 }
    	
    	
        // Create tables again
        //onCreate(db);
    }
    
}
