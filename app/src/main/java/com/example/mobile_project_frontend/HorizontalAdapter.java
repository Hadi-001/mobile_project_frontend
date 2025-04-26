package com.example.mobile_project_frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private List<HorizontalItem> itemList;

    public HorizontalAdapter(List<HorizontalItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HorizontalItem item = itemList.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.favoriteButton.setTag(item.getImageResId());
        holder.titleTextView.setText(item.getTitle());
        holder.propertyTitleTextView.setText(item.getPropertyTitle());
        holder.locationTextView.setText(item.getLocation());
        holder.bedroomCountTextView.setText(String.valueOf(item.getBedroomCount()));
        holder.bathroomCountTextView.setText(String.valueOf(item.getBathroomCount()));
        holder.priceTextView.setText(item.getPrice());


        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageButton button = (ImageButton) v;
                Object tag = button.getTag();
                if (tag != null && (int) tag == R.drawable.ic_heart_filled) {
                    button.setImageResource(R.drawable.ic_heart_empty);
                    button.setTag(R.drawable.ic_heart_empty);
                } else {
                    button.setImageResource(R.drawable.ic_heart_filled);
                    button.setTag(R.drawable.ic_heart_filled);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView titleTextView, propertyTitleTextView, locationTextView, bedroomCountTextView, bathroomCountTextView, priceTextView;
        ImageButton favoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            propertyTitleTextView = itemView.findViewById(R.id.propertyTitleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            bedroomCountTextView = itemView.findViewById(R.id.bedroomCountTextView);
            bathroomCountTextView = itemView.findViewById(R.id.bathroomCountTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);
        }
    }
}
