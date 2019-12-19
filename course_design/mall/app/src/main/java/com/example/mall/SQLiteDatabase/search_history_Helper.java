package com.example.mall.SQLiteDatabase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class search_history_Helper extends SQLiteOpenHelper {
    public search_history_Helper(Context context) {
        super(context, "search_history.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table search_history(id integer primary key autoincrement,desc varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}