package com.example.phuct.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.phuct.myapplication.R;
import com.example.phuct.myapplication.model.Image;

import java.util.ArrayList;

/**
 * Created by phuct on 1/19/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    private ArrayList<Image> listImage;
    private Context context;

    public ImageAdapter(ArrayList<Image> listImage, Context context) {
        this.listImage = listImage;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.recycler_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = listImage.get(position);
        Glide.with(context).load(image.getURL()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
