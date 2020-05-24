package com.example.homework2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class WaitlistDbHelper extends SQLiteOpenHelper {
    //Always control the database here

    private static final String DATABASE_NAME = "waitlist.db";

    private static final int DATABASE_VERSION = 1;

    //Constructor that takes a context and calls the parent constructor
    public WaitlistDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + WaitlistContract.WaitlistEntry.TABLE_NAME + " (" +
                WaitlistContract.WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
        //.execSQL used to run the query
        db.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WaitlistContract.WaitlistEntry.TABLE_NAME);
        onCreate(db);
    }

    //create the addData function here so that we can add data from different activities
    public boolean addData(String name, int size){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //put name into COLUMN_GUEST_NAME
        contentValues.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        //put size into COLUMN_PARTY_SIZE
        contentValues.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, size);

        Log.d("Add Data", "adding " + name + " and " + String.valueOf(size) + " to " + WaitlistContract.WaitlistEntry.TABLE_NAME);

        long result = db.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, contentValues);
        //if the data is inserted incorrectly, it will return -1
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    //Return the data from the database
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.query(WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP);
    }

    public boolean removeData(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }
}
