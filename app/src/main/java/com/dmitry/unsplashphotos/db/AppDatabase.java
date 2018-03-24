package com.dmitry.unsplashphotos.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dmitry.unsplashphotos.entities.ImageItem;
import com.dmitry.unsplashphotos.entities.dao.ImageItemDAO;

@Database(entities = {ImageItem.class}, version = 1)
abstract class AppDatabase extends RoomDatabase {
    public abstract ImageItemDAO employeeDao();
}
