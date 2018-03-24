package com.dmitry.unsplashphotos.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dmitry.unsplashphotos.IO.ImageIOMapper;
import com.dmitry.unsplashphotos.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

    private Context context;
    private int layoutResourceId;
    private ArrayList path = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList path) {
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.path = path;
    }

    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
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
        //load image by path, and insert after load
        ImageIOMapper imageIOMapper = new ImageIOMapper();
        imageIOMapper.loadImageWithGlide(context, path.get(position).toString(), holder.image);
        return row;
    }

    private static class ViewHolder {
        ImageView image;
    }
}