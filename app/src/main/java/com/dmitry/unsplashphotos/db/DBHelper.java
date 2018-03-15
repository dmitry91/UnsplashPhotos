package com.dmitry.unsplashphotos.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "UnsplashDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB", "onCreate database");

        db.execSQL("create table saved ("
                + "id text primary key,"
                + "description text,"
                + "regularUrl text, "
                + "thumbUrl text, "
                +"UNIQUE( 'id' )"
                +"ON CONFLICT REPLACE );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
