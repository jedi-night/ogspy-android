package com.ogsteam.ogspy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ogsteam.ogspy.data.models.Message;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMessagesHandler extends DatabaseHandler {
    private static final int NOMBRE_MAXIMUM_DE_MESSAGES = 20;

	public DatabaseMessagesHandler(Context context) {
		super(context);
	}

	/**
	 * Add a new ogspy message
	 * 
	 * @param message
	 */
	public long addMessage(Message message) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_MESSAGES_ID, message.getId());
        values.put(KEY_MESSAGES_DATE, message.getDatetime());
		values.put(KEY_MESSAGES_SENDER, message.getSender());
		values.put(KEY_MESSAGES_CONTENT, message.getContent());

		// Inserting Row
        List<Message> messages = getAllMessages();
        if(messages.size() > NOMBRE_MAXIMUM_DE_MESSAGES){
            deleteMessage(messages.get(0));
        }
		long result = db.insert(TABLE_MESSAGES, null, values);

		// Closing database connection
		db.close();
		return result;
	}
	 
	
	public int updateMessage(Message message) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
        values.put(KEY_MESSAGES_ID, message.getId());
        values.put(KEY_MESSAGES_DATE, message.getDatetime());
        values.put(KEY_MESSAGES_SENDER, message.getSender());
        values.put(KEY_MESSAGES_CONTENT, message.getContent());
		
	    // updating row
	    int result = db.update(TABLE_MESSAGES, values, KEY_MESSAGES_ID + " = ?", new String[] { String.valueOf(message.getId()) });
	    db.close();	    
	    return result;
	}
	
	public void deleteMessage(Message message) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_MESSAGES, KEY_MESSAGES_ID + " = ?",
	            new String[] { String.valueOf(message.getId()) });
	    db.close();
	}
	
	public void deleteAllMessages() {
	    SQLiteDatabase db = this.getWritableDatabase();
	    
	    db.delete(TABLE_MESSAGES, null, null);
	    db.close();
	}
	
	/**
	 * Get message by it's id
	 * 
	 * @param id
	 * @return
	 */
	public Message getMessageById(int id) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    Cursor cursor = db.query(TABLE_MESSAGES, new String[] { KEY_MESSAGES_ID, KEY_MESSAGES_DATE, KEY_MESSAGES_SENDER, KEY_MESSAGES_CONTENT }, KEY_MESSAGES_ID + "=?",
	            new String[] { String.valueOf(id) }, null, null, null, null);
	    if (cursor != null) {
	        cursor.moveToFirst();
	    }

        Message message = new Message(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
	    return message;
	}
	
	/**
	 * Get all messages ogspy
	 * 
	 * @return
	 */
    public List<Message> getAllMessages() {
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + " ORDER BY " + KEY_MESSAGES_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setDatetime(cursor.getString(1));
                message.setSender(cursor.getString(2));
                message.setContent(cursor.getString(3));
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        // return message list
        return messageList;
    }

    /**
     * Get all messages ogspy
     *
     * @return
     */
    public List<Message> getAllMessagesDesc() {
        List<Message> messageList = new ArrayList<Message>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + " ORDER BY " + KEY_MESSAGES_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Message message = new Message();
                message.setId(Integer.parseInt(cursor.getString(0)));
                message.setDatetime(cursor.getString(1));
                message.setSender(cursor.getString(2));
                message.setContent(cursor.getString(3));
                // Adding message to list
                messageList.add(message);
            } while (cursor.moveToNext());
        }

        // return message list
        return messageList;
    }
    public int getNextMessageId() {
        List<Message> messageList = getAllMessages();
        if(messageList.isEmpty()){
            return 0;
        }
        return messageList.get(messageList.size()-1).getId() + 1;
    }

	
	/**
	 *  How messages ?
	 * @return
	 */
	
	public int getMessagessCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGES;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        
        // return count
        return cursor.getCount();
    }
}
