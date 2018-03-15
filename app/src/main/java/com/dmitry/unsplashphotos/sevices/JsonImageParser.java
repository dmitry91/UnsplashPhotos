package com.dmitry.unsplashphotos.sevices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dmitry.unsplashphotos.entities.ImageItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class JsonImageParser {
    //return list ImageItem from url json response
    public ArrayList<ImageItem> getArrayImageItem(String url){

        ArrayList<ImageItem> imageItemArray = new ArrayList<>();
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject p = jsonArray.getJSONObject(i);

                    String id = p.getString("id");
                    String description = p.getString("description");
                    JSONObject urls = p.getJSONObject("urls");
                    String regularPhotoUrl = urls.getString("regular");
                    String thumbPhotoUrl = urls.getString("thumb");

                    imageItemArray.add(new ImageItem(id,description,regularPhotoUrl,thumbPhotoUrl));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return imageItemArray;
    }

    public Bitmap getImageReduce(String urlPath){
        Bitmap bmp = null;
        try {
            URL url = new URL(urlPath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//reduce the original image 2 times, else out of memory error
            bmp = BitmapFactory.decodeStream((InputStream) url.getContent(), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public Bitmap getImage(String urlPath){
        Bitmap bmp = null;
        try {
            URL url = new URL(urlPath);
            bmp = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
