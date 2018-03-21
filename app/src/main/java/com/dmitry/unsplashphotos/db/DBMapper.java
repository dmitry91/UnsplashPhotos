package com.dmitry.unsplashphotos.db;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dmitry.unsplashphotos.entities.ImageItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBMapper {

    private ContentValues cv;
    private SQLiteDatabase db;

    public DBMapper(Activity activity) {
        cv = new ContentValues();
        DBHelper dbHelper = new DBHelper(activity);
        db = dbHelper.getWritableDatabase();
    }

    public long insertToSavedTable(ImageItem imageItem){
        cv.put("id", imageItem.getId());
        cv.put("description", imageItem.getDescription());
        cv.put("regularUrl", imageItem.getRegularPhotoUrl());
        cv.put("thumbUrl", imageItem.getThumbPhotoUrl());
        return db.insert("saved", null, cv);
    }

    public ArrayList<ImageItem> readFromSavedTable(){
        ArrayList<ImageItem> result = new ArrayList<>();
        Cursor c = db.query("saved", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int descriptionColIndex = c.getColumnIndex("description");
            int regularColIndex = c.getColumnIndex("regularUrl");
            int thumbColIndex = c.getColumnIndex("thumbUrl");
            do {
                ImageItem imageItem = new ImageItem(c.getString(idColIndex),c.getString(descriptionColIndex),
                        new ImageItem.Urls(null, null,c.getString(regularColIndex),null,c.getString(thumbColIndex)));
                result.add(imageItem);
            } while (c.moveToNext());
        } else
            Log.d("Read table saved:", "0 rows");
        c.close();
        return result;
    }

    public int deleteRowFromSaved(String id){
        return db.delete("saved", "id = " +"\'"+ id+"\'", null);
    }

}
