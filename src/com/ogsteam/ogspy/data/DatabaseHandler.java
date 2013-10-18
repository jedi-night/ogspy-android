package com.ogsteam.ogspy.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 5;
 
    // Database Name
    private static final String DATABASE_NAME = "ogspy";

    // Table names
    protected static final String TABLE_ACCOUNTS = "accounts";
    protected static final String TABLE_PREFS = "preferences";
    protected static final String TABLE_MESSAGES = "messages";

    // Messages Table Columns names
    protected static final String KEY_MESSAGES_ID = "id";
    protected static final String KEY_MESSAGES_DATE = "date";
    protected static final String KEY_MESSAGES_SENDER = "sender";
    protected static final String KEY_MESSAGES_CONTENT = "content";

    // Account Table Columns names
    protected static final String KEY_ACCOUNT_ID = "id";
    protected static final String KEY_ACCOUNT_USERNAME = "username";
    protected static final String KEY_ACCOUNT_PASSWORD = "password";
    protected static final String KEY_ACCOUNT_SERVER_URL = "server_url";
    protected static final String KEY_ACCOUNT_SERVER_UNIVERS = "server_univers";
    
    // Prefs table name
    protected static final String KEY_PREFS_ID = "id";
    protected static final String KEY_PREFS_REFRESH_HOSTILES_TIME = "refresh_hostiles_time";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        createAccountTable(db);
        createPreferencesTable(db);
        createMessageTable(db);
    }
 
    private void createAccountTable(SQLiteDatabase db){
    	String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_ACCOUNT_USERNAME + " TEXT,"
                + KEY_ACCOUNT_PASSWORD + " TEXT," 
                + KEY_ACCOUNT_SERVER_URL + " TEXT,"
                + KEY_ACCOUNT_SERVER_UNIVERS + " TEXT"
        		+ ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }
 
    private void createPreferencesTable(SQLiteDatabase db){
    	String CREATE_PREFS_TABLE = "CREATE TABLE " + TABLE_PREFS + "("
                + KEY_PREFS_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_PREFS_REFRESH_HOSTILES_TIME + " INTEGER"
        		+ ")";
        db.execSQL(CREATE_PREFS_TABLE);
    }

    private void createMessageTable(SQLiteDatabase db){
        String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGES + "("
                + KEY_MESSAGES_ID + " INTEGER PRIMARY KEY,"
                + KEY_MESSAGES_DATE + " TEXT,"
                + KEY_MESSAGES_SENDER + " TEXT,"
                + KEY_MESSAGES_CONTENT + " TEXT"
                + ")";
        db.execSQL(CREATE_MESSAGES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		if(oldVersion == 1){
			 db.execSQL("ALTER TABLE " + TABLE_ACCOUNTS + " ADD "+ KEY_ACCOUNT_SERVER_UNIVERS + " TEXT");
		} else if(oldVersion == 2){
			createPreferencesTable(db);
        } else if(oldVersion == 3){
            createMessageTable(db);
        } else if(oldVersion == 4){
            db.execSQL("DROP TABLE " + TABLE_MESSAGES);
            createMessageTable(db);
        }
    	
        // Create tables again
        //onCreate(db);
    }
    
}
