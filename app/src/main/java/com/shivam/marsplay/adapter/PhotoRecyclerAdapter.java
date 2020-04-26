package com.shivam.marsplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shivam.marsplay.R;
import com.shivam.marsplay.databinding.ItemPhotoBinding;

import java.util.ArrayList;

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> urlArrayList;

    public PhotoRecyclerAdapter(Context context, ArrayList<String> urlArrayList) {
        this.context = context;
        this.urlArrayList = urlArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemPhotoBinding itemBinding = ItemPhotoBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(urlArrayList.get(position))
                .centerCrop()
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .fallback(R.drawable.ic_loading)
                .into(holder.itemPhotoBinding.ivPhoto);

    }

    @Override
    public int getItemCount() {
        return urlArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemPhotoBinding itemPhotoBinding;

        public ViewHolder(@NonNull ItemPhotoBinding itemPhotoBinding) {
            super(itemPhotoBinding.getRoot());
            this.itemPhotoBinding = itemPhotoBinding;
        }
    }
}
