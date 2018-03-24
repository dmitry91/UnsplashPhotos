package com.dmitry.unsplashphotos.db;

import com.dmitry.unsplashphotos.entities.ImageItem;
import com.dmitry.unsplashphotos.entities.dao.ImageItemDAO;

import java.util.ArrayList;

public class DBMapper {

    private AppDatabase db = App.getInstance().getDatabase();
    private ImageItemDAO imageItemDAO= db.employeeDao();

    public DBMapper() {}

    public void insertToImageItemTable(ImageItem imageItem){
        imageItemDAO.insert(imageItem);
    }

    public ArrayList<ImageItem> readFromImageItemTable(){
        return (ArrayList<ImageItem>) imageItemDAO.getAll();
    }

    public void deleteRowFromImageTable(String id){
        imageItemDAO.delete(imageItemDAO.getById(id));
    }

}
