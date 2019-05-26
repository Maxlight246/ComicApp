package com.example.comicapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comicapp.Common.Common;
import com.example.comicapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

    Context context;
    List<String> imageLinks;

    public MyViewPagerAdapter(Context context, List<String> linkList) {
        this.context = context;
        this.imageLinks = linkList;
    }

    @Override
    public int getCount() {
        return imageLinks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_pager_item,container,false);

        PhotoView page_image = view.findViewById(R.id.page_image);
        Picasso.get().load(imageLinks.get(position)).into(page_image);

        container.addView(view);
        return view;
    }
}