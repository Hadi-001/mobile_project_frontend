package com.example.mobile_project_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MyPropertiesAdapter extends RecyclerView.Adapter<MyPropertiesAdapter.ViewHolder> {

    private List<PropertyItem> itemList;
    private Context context;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClicked(PropertyItem property);
    }

    public MyPropertiesAdapter(List<PropertyItem> itemList, Context context, OnDeleteClickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public MyPropertiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estates_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPropertiesAdapter.ViewHolder holder, int position) {
        PropertyItem item = itemList.get(position);

        holder.titleTextView.setText(item.getType());

        if (holder.imageView != null) {
            Glide.with(context)
                    .load("http://10.0.2.2/" + item.getImageLink())
                    .into(holder.imageView);
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClicked(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            deleteButton = itemView.findViewById(R.id.favoriteButton);  // Use the same button id
        }
    }
}
