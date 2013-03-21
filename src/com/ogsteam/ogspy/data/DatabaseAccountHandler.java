package com.ogsteam.ogspy.data;

import java.util.ArrayList;
import java.util.List;

import com.ogsteam.ogspy.data.models.Account;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccountHandler extends DatabaseHandler {

	public DatabaseAccountHandler(Context context) {
		super(context);
	}

	/**
	 * Add a new ogspy account
	 * 
	 * @param account
	 */
	public long addAccount(Account account) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, account.getId());
		values.put(KEY_USERNAME, account.getUsername());
		values.put(KEY_PASSWORD, account.getPassword());
		values.put(KEY_SERVER_URL, account.getServerUrl());
		values.put(KEY_SERVER_UNIVERS, account.getServerUnivers());
		
		// Inserting Row
		long result = db.insert(TABLE_ACCOUNTS, null, values);
		// Closing database connection
		db.close();
		return result;
	}
	 
	
	public int updateAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(KEY_USERNAME, account.getUsername());
		values.put(KEY_PASSWORD, account.getPassword());
		values.put(KEY_SERVER_URL, account.getServerUrl());
		values.put(KEY_SERVER_UNIVERS, account.getServerUnivers());
		
	    // updating row
	    int result = db.update(TABLE_ACCOUNTS, values, KEY_ID + " = ?", new String[] { String.valueOf(account.getId()) });	    
	    db.close();	    
	    return result;
	}
	
	public void deleteAccount(Account account) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_ACCOUNTS, KEY_ID + " = ?",
	            new String[] { String.valueOf(account.getId()) });
	    db.close();
	}
	
	public void deleteAllAccounts() {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_ACCOUNTS, null, null);
	    db.close();
	}
	
	/**
	 * Get account by it's id
	 * 
	 * @param id
	 * @return
	 */
	public Account getAccountById(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_SERVER_URL, KEY_SERVER_UNIVERS }, KEY_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null) {
	        cursor.moveToFirst();
	    }
	 
	    Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
	    return account;
	}
	
	/**
	 * Get all accounts ogspy
	 * 
	 * @return
	 */
	public List<Account> getAllAccounts() {
	    List<Account> contactList = new ArrayList<Account>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
	 
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	 
	    // looping through all rows and adding to list
	    if (cursor.moveToFirst()) {
	        do {
	        	Account contact = new Account();
	            contact.setId(Integer.parseInt(cursor.getString(0)));
	            contact.setUsername(cursor.getString(1));
	            contact.setPassword(cursor.getString(2));
	            contact.setServerUrl(cursor.getString(3));
	            contact.setServerUnivers(cursor.getString(4));
	            // Adding account to list
	            contactList.add(contact);
	        } while (cursor.moveToNext());
	    }
	    
	    // return contact list
	    return contactList;
	}
	
	/**
	 *  How accounts ? 
	 * @return
	 */
	
	public int getAccountsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        
        // return count
        return cursor.getCount();
    }
}
