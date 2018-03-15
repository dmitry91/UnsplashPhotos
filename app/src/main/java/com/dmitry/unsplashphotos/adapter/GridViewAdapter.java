package com.dmitry.unsplashphotos.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dmitry.unsplashphotos.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //use element
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) row.findViewById(R.id.image_grid);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.image.setImageBitmap((Bitmap) data.get(position));
        return row;
    }

    private static class ViewHolder {
        ImageView image;
    }
}