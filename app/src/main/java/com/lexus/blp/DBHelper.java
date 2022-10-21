package com.lexus.blp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "blpointage.db";
    private static final String TABLE_AGENT = "agents";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_AGENT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password Text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        // Create tables again
        onCreate(db);
    }

    public Boolean insertData(String username, String password ){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues  values = new ContentValues();


        values.put("username", username);
        values.put("password", password);

        long result =  db.insert(TABLE_AGENT, null, values);

        if(result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkusername(String username ){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_AGENT + " where username=?", new String[] {username});

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkusernamepassword(String username, String password ){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_AGENT + " where username=? and password=?", new String[] {username, password});

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }
}
