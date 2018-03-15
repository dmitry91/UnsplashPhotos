package com.dmitry.unsplashphotos;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.dmitry.unsplashphotos.adapter.ViewPagerAdapter;
import com.dmitry.unsplashphotos.entities.ImageItem;

import java.util.ArrayList;

public class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_details);

        final int position = getIntent().getIntExtra("position",0);
        final ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) getIntent().getSerializableExtra("array");

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_details);

        PagerAdapter adapter = new ViewPagerAdapter(DetailsActivity.this, imageItems, getIntent().getStringExtra("tab"));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

}
