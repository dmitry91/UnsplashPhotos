package com.dmitry.unsplashphotos.IO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.dmitry.unsplashphotos.sevices.UnsplashApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageIOMapper {

    private UnsplashApi mUnsplashApi;
    private String rootPath;

    public ImageIOMapper() {
        mUnsplashApi = new UnsplashApi();
        rootPath = Environment.getExternalStorageDirectory().toString();
    }

    public void saveToInternalStorage(String url, String name){
        Bitmap bitmap = mUnsplashApi.getImage(url);

        File myDir = new File(rootPath +"/UnsplashPhotos/saved");
        boolean createDir = true;
        if (!myDir.exists()){
            createDir = myDir.mkdirs();
        }
        if(createDir) {
            File file = new File(myDir, name + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap loadImageFromStorage(String name)
    {
        FileInputStream fileInputStream;
        File myDir = new File(rootPath +"/UnsplashPhotos/saved");

        File file = new File(myDir,  name +".jpg");
        Bitmap bitmap = null;
        try{
            fileInputStream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void delete(String name){
        File fdelete = new File(rootPath +"/UnsplashPhotos/saved/" + name +".jpg");
        if (fdelete.exists()) {
            fdelete.delete();
        }
    }

    public static boolean createDIr(String path){
        File myDir = new File(path);
        System.out.println(myDir.mkdirs()+" rootPath +\"/UnsplashPhotos/saved\"");
            return myDir.mkdirs();
    }
}
