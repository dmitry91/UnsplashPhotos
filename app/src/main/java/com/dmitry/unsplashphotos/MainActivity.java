package com.dmitry.unsplashphotos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.Toast;

import com.dmitry.unsplashphotos.IO.ImageIOMapper;
import com.dmitry.unsplashphotos.adapter.GridViewAdapter;
import com.dmitry.unsplashphotos.db.DBMapper;
import com.dmitry.unsplashphotos.entities.ImageItem;
import com.dmitry.unsplashphotos.sevices.JsonImageParser;
import com.dmitry.unsplashphotos.sevices.ServiceManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String TAB_1 = "tag1";
    public static final String TAB_2 = "tag2";
    public static final String URL_UNSPLASH_PHOTOS = "https://api.unsplash.com/photos/" +
            "?client_id=d0ab6ac8477c4b76568e66db73d043e5b441c0a0a64a1eab8e01ca69d26ef0d1" +
            "&per_page=30&page=1&order_by=popular";
    private ArrayList<ImageItem> imageItems;
    private static MainActivity instance;

    @Override
    protected void onStart() {
        super.onStart();

        createTab1();
        createTab2();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tabSpec;
        // create tab 1
        tabSpec = tabHost.newTabSpec(TAB_1);
        tabSpec.setIndicator("List photos");
        tabSpec.setContent(R.id.layoutTab1);
        // add to the root element
        tabHost.addTab(tabSpec);
        // create tab 2
        tabSpec = tabHost.newTabSpec(TAB_2);
        tabSpec.setIndicator("Saved");
        tabSpec.setContent(R.id.layoutTab2);
        tabHost.addTab(tabSpec);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String tabId) {

                switch (tabId) {
                    case TAB_1:
                        Toast.makeText(MainActivity.this, "Use List photos", Toast.LENGTH_LONG).show();
                        break;
                    case TAB_2:
                        Toast.makeText(MainActivity.this, "Use Saved", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    public void createTab1() {
        imageItems = null;
        ArrayList<Bitmap> arrayBitmap = null;
        DownloadImageTask downloadImageTask = new DownloadImageTask();
        try {
            arrayBitmap = downloadImageTask.execute(URL_UNSPLASH_PHOTOS).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        GridView gridView = (GridView) findViewById(R.id.gridViewTab1);
        GridViewAdapter gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, arrayBitmap);
        gridView.setAdapter(gridAdapter);
        //Start details activity
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("array", imageItems);
                intent.putExtra("tab", "tab1");
                startActivity(intent);
            }
        });
    }

    private void createTab2() {
        ArrayList<ImageItem> imageItemsTab2;
        ArrayList<Bitmap> arrayBitmapTab2 = new ArrayList<>();
        ImageIOMapper imageIOMapper = new ImageIOMapper();
        DBMapper dbMapper = new DBMapper(this);

        imageItemsTab2 = dbMapper.readFromSavedTable();
        for (int i = 0; i < imageItemsTab2.size(); i++) {
            arrayBitmapTab2.add(imageIOMapper.loadImageFromStorage(imageItemsTab2.get(i).getThumbPhotoName()));
        }

        GridView gridViewTab2 = (GridView) findViewById(R.id.gridViewTab2);
        GridViewAdapter gridAdapterTab2 = new GridViewAdapter(this, R.layout.grid_item_layout, arrayBitmapTab2);
        gridViewTab2.setAdapter(gridAdapterTab2);
        //Start details activity
        final ArrayList<ImageItem> finalImageItemsTab = imageItemsTab2;
        gridViewTab2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("array", finalImageItemsTab);
                intent.putExtra("tab", "tab2");
                startActivity(intent);
            }
        });
    }

    public static MainActivity getInstance(){
        return instance;
    }

    //for download json array from unsplash, and parse json array to ImageItem
    private class DownloadImageTask extends AsyncTask<String, Object, ArrayList<Bitmap>> {
        ArrayList<Bitmap> arrayBitmap;

        DownloadImageTask() {
            arrayBitmap = new ArrayList<>();
        }

        protected ArrayList<Bitmap> doInBackground(String... urls) {
            imageItems = new JsonImageParser().getArrayImageItem(urls[0]);
            for (int i = 0; i < imageItems.size(); i++) {
                Bitmap bmp = null;
                try {
                    URL url = new URL(imageItems.get(i).getThumbPhotoUrl());
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                arrayBitmap.add(bmp);
            }
            return arrayBitmap;
        }
    }

    public static class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if(checkInternet(context))
            {
                Toast.makeText(context, "Network Available Do operations",Toast.LENGTH_LONG).show();
                MainActivity.getInstance().onStart();//update activity after internet connection
            }
        }
        boolean checkInternet(Context context) {
            ServiceManager serviceManager = new ServiceManager(context);
            return serviceManager.isNetworkAvailable();
        }
    }
}
