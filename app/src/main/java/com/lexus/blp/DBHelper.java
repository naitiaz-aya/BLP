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
    public static final String TABLE_NAME = "pointage_table";
    // pointage table Columns name
    public static final String COl_1 = "idpt";
    public static final String COl_2 = "idblp";
    public static final String COl_3 = "datePointage";
    public static final String COl_4 = "lat";
    public static final String COl_5= "agent";
    public static final String COl_6 = "intime";
    public static final String COl_7 = "inzone";
    public static final String COl_8 = "valide";
    public static final String COl_9 = "matriculeOK";
    public static final String COl_10 = "matriculeLu";
    public static final String COl_11 = "longi";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_AGENT + " (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password Text )");
        db.execSQL("create table " + TABLE_NAME + "("+COl_1+" INTEGER PRIMARY KEY AUTOINCREMENT , "+COl_2+" TEXT , "+COl_3+" TEXT, "+COl_4+" TEXT,"+COl_11+" TEXT, "+COl_5+" TEXT, "+COl_6+" BOOLEAN, "+COl_7+" BOLLEAN , "+COl_8+" BOOLEAN ,"+COl_9+" BOOLEAN, "+COl_10+" BOOLEAN )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AGENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public boolean insertDataIntoTheDB( int idblp, String datePointage, String lat, String longi, String agent, Boolean intime, Boolean inzone, Boolean valide, Boolean matriculeOK, Boolean matriculeLu) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COl_2, idblp);
        contentValues.put(COl_3, datePointage);
        contentValues.put(COl_4, lat);
        contentValues.put(COl_5, agent);
        contentValues.put(COl_6, intime? 1: 0);
        contentValues.put(COl_7, inzone? 1: 0);
        contentValues.put(COl_8, valide? 1: 0);
        contentValues.put(COl_9, matriculeOK? 1: 0);
        contentValues.put(COl_10, matriculeLu? 1: 0);
        contentValues.put(COl_11, longi);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return (result == -1) ? false : true;

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
