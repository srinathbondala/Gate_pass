package com.example.easyexit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int[] images;
   // String[] imageNames;
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] images)
    {
        this.context = applicationContext;
        this.images = images;
      //  this.imageNames = imageNames;
        inflater =(LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.flipper_items,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.images);
        imageView.setImageResource(images[i]);
        return view;
    }
}
