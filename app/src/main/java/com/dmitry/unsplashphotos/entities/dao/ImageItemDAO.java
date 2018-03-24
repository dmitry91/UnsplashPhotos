package com.dmitry.unsplashphotos.entities.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dmitry.unsplashphotos.entities.ImageItem;

import java.util.List;

@Dao
public interface ImageItemDAO {
    @Query("SELECT * FROM imageitem")
    List<ImageItem> getAll();

    @Query("SELECT * FROM imageitem WHERE id = :id")
    ImageItem getById(String id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ImageItem item);

    @Update
    void update(ImageItem item);

    @Delete
    void delete(ImageItem item);
}
