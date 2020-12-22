package com.alakamandawalk.webtoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "WebDb.db";
    public static final String TABLE_NAME = "web_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    private HashMap hp;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table web_items " + "(id integer primary key, name text, url text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS web_items");
        onCreate(db);
    }

    void addWebsite(Websites websites) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, websites.getName());
        values.put(COLUMN_URL, websites.getUrl());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from web_items where id="+id+"", null );
        return res;
    }

    public Integer deleteWebsite (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("web_items",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    ArrayList<Websites> getAllWebsites() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Websites> storeWebsites = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String url = cursor.getString(2);
                storeWebsites.add(new Websites(id, name, url));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeWebsites;
    }
}
