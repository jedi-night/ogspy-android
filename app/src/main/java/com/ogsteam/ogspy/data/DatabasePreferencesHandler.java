package com.ogsteam.ogspy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ogsteam.ogspy.data.models.Prefs;

import java.util.ArrayList;
import java.util.List;

public class DatabasePreferencesHandler extends DatabaseHandler {

	public DatabasePreferencesHandler(Context context) {
		super(context);
	}

	/**
	 * Add a new ogspy Prefs
	 * 
	 * @param Settings
	 */
	public long addPrefs(Prefs prefs) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PREFS_ID, prefs.getId());
		values.put(KEY_PREFS_REFRESH_HOSTILES_TIME, prefs.getRefreshHostiles());
		
		// Inserting Row
		long result = db.insert(TABLE_PREFS, null, values);
		// Closing database connection
		db.close();
		return result;
	}
	 
	
	public int updatePrefs(Prefs prefs) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_PREFS_ID, prefs.getId());
		values.put(KEY_PREFS_REFRESH_HOSTILES_TIME, prefs.getRefreshHostiles());
				
	    // updating row
	    int result = db.update(TABLE_PREFS, values, KEY_PREFS_ID + " = ?", new String[] { String.valueOf(prefs.getId()) });	    
	    db.close();	    
	    return result;
	}
	
	public void deletePrefs(Prefs prefs) {
	    SQLiteDatabase db = this.getWritableDatabase();	    
	    db.delete(TABLE_PREFS, KEY_ACCOUNT_ID + " = ?", new String[] { String.valueOf(prefs.getId()) });
	    db.close();
	}
	
	public void deleteAllPrefs() {
	    SQLiteDatabase db = this.getWritableDatabase();	    
	    db.delete(TABLE_PREFS, null, null);
	    db.close();
	}
	
	/**
	 * Get Prefs by it's id
	 * 
	 * @param id
	 * @return
	 */
	public Prefs getPrefsById(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_PREFS, new String[] { KEY_PREFS_ID, KEY_PREFS_REFRESH_HOSTILES_TIME }, KEY_PREFS_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null) {
	        cursor.moveToFirst();
	    }
	 
	    Prefs Prefs = new Prefs(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)));
	    return Prefs;
	}
	
	/**
	 * Get all Prefs ogspy
	 * 
	 * @return
	 */
	public List<Prefs> getAllPrefs() {
	    List<Prefs> prefsList = new ArrayList<Prefs>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_PREFS;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Prefs prefs = new Prefs();
	        	prefs.setId(Integer.parseInt(cursor.getString(0)));
	        	prefs.setRefreshHostiles(Integer.parseInt(cursor.getString(1)));
	            // Adding Prefs to list
	        	prefsList.add(prefs);
	        } while (cursor.moveToNext());
	    }	    
	    // return prefs list
	    return prefsList;
	}
	
	/**
	 *  How Prefs ? 
	 * @return
	 */
	
	public int getPrefsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PREFS;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        
        // return count
        return cursor.getCount();
    }
}
