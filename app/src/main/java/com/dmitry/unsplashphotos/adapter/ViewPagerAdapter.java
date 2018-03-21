package com.dmitry.unsplashphotos.adapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dmitry.unsplashphotos.DetailsActivity;
import com.dmitry.unsplashphotos.IO.ImageIOMapper;
import com.dmitry.unsplashphotos.R;
import com.dmitry.unsplashphotos.db.DBMapper;
import com.dmitry.unsplashphotos.entities.ImageItem;
import com.dmitry.unsplashphotos.sevices.UnsplashApi;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private String tabPos;
    private ArrayList<ImageItem> imageItems;
    private DBMapper dBMapper;
    private ImageIOMapper mImageIOMapper;

    public ViewPagerAdapter(Context context, ArrayList<ImageItem> imageItems, String tabPos) {
        this.imageItems = imageItems;
        this.context = context;
        this.tabPos = tabPos;
        dBMapper = new DBMapper((Activity) context);
        mImageIOMapper = new ImageIOMapper();
    }

    @Override
    public int getCount() {
        return imageItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        TextView descriptionView;
        final ImageView imageView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.details_activity, container,
                false);

        descriptionView = (TextView) itemView.findViewById(R.id.title_details);
        if (imageItems.get(position).getDescription() != null && !imageItems.get(position).getDescription().equals("null")) {
            descriptionView.setText(imageItems.get(position).getDescription());
        }

        imageView = (ImageView) itemView.findViewById(R.id.image_details);

        DownloadImageBitmap downloadImageBitmap = new DownloadImageBitmap();
        Bitmap imageBtm = null;
        if (tabPos.equals("tab1")) {
            try {
                imageBtm = downloadImageBitmap.execute(imageItems.get(position).getRegularPhotoUrl()).get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        if (tabPos.equals("tab2")) {
            imageBtm = mImageIOMapper.loadImageFromStorage(imageItems.get(position).getRegularPhotoName());
        }

        imageView.setImageBitmap(imageBtm);

        Button mButton = (Button) itemView.findViewById(R.id.btn_details);
        if (tabPos.equals("tab1")) {
            mButton.setText(R.string.btn_save);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                SaveImage saveImage = new SaveImage();
                    try {
                        if(saveImage.execute(position).get()){
                            Toast.makeText(imageView.getContext(), "save", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            mButton.setText(R.string.btn_delete);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dBMapper.deleteRowFromSaved(imageItems.get(position).getId());
                    mImageIOMapper.delete(imageItems.get(position).getThumbPhotoName());
                    mImageIOMapper.delete(imageItems.get(position).getRegularPhotoName());
                    Toast.makeText(imageView.getContext(), "delete", Toast.LENGTH_LONG).show();
                }
            });
        }

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    private class DownloadImageBitmap extends AsyncTask<String, Object, Bitmap> {
        UnsplashApi mUnsplashApi;

        DownloadImageBitmap() {
            mUnsplashApi = new UnsplashApi();
        }

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected Bitmap doInBackground(String... urls) {
            if (tabPos.equals("tab1")) {
                return mUnsplashApi.getImageReduce(urls[0]);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class SaveImage extends AsyncTask<Integer, Object, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            if(((DetailsActivity)context).hasPermissions()) {
                //save to database
                dBMapper.insertToSavedTable(imageItems.get(params[0]));
                //save image to local storage
                mImageIOMapper.saveToInternalStorage(imageItems.get(params[0]).getThumbPhotoUrl(), imageItems.get(params[0]).getThumbPhotoName());
                mImageIOMapper.saveToInternalStorage(imageItems.get(params[0]).getRegularPhotoUrl(), imageItems.get(params[0]).getRegularPhotoName());
                return true;
            }
            else {
                ((DetailsActivity)context).requestPermissionWithRationale();
                return false;
            }
        }
    }

}