package com.dmitry.unsplashphotos.IO;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmitry.unsplashphotos.sevices.UnsplashApi;

import java.io.File;
import java.io.FileOutputStream;

public class ImageIOMapper {

    private UnsplashApi mUnsplashApi;
    public static String rootPathStorage = Environment.getExternalStorageDirectory() + "/UnsplashPhotos/saved";

    public ImageIOMapper() {
        mUnsplashApi = new UnsplashApi();
    }

    public void saveToInternalStorage(String url, String name) {
        Bitmap bitmap = mUnsplashApi.loadImage(url);

        File myDir = new File(rootPathStorage);
        boolean createDir = true;
        if (!myDir.exists()) {
            createDir = myDir.mkdirs();
        }
        if (createDir) {
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

    public void loadImageWithGlide(Context context, String path, ImageView imageView) {
        Glide.
                with(context).
                load(path).
                asBitmap().
                into(imageView);
    }

    public void delete(String name) {
        File fileDelete = new File(rootPathStorage + "/" + name + ".jpg");
        if (fileDelete.exists()) {
            fileDelete.delete();
        }
    }

    public static boolean createDIr(String path) {
        File myDir = new File(path);
        return myDir.mkdirs();
    }
}
