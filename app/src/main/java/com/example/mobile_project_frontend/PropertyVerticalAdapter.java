package com.example.mobile_project_frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PropertyVerticalAdapter extends RecyclerView.Adapter<PropertyVerticalAdapter.PropertyViewHolder> {
    private List<PropertyItem> items;
    private Context context;

    public PropertyVerticalAdapter(List<PropertyItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    // ViewHolder as an inner class
    public class PropertyViewHolder extends RecyclerView.ViewHolder {
        public int propertyId;
        private ImageButton favoriteButton;

        public PropertyViewHolder(View itemView) {
            super(itemView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            // Set click listener for the entire card
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (propertyId >= 1) {
                        Intent intent = new Intent(context, PropertyDetailActivity.class);
                        intent.putExtra("PROPERTY_ID", propertyId);
                        context.startActivity(intent);
                    }
                }
            });

            // Set click listener for favorite button
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isSelected = !favoriteButton.isSelected();
                    favoriteButton.setSelected(isSelected);

                    if (isSelected) {
                        favoriteButton.setImageResource(R.drawable.ic_heart_filled);
                    } else {
                        favoriteButton.setImageResource(R.drawable.ic_heart_empty);
                    }

                    // Update in database if needed
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        items.get(position).setFavorite(isSelected);
                    }
                }
            });
        }

        public void bindData(PropertyItem item) {
            this.propertyId = item.getEstateId();
            boolean isFavorite = item.isFavorite();
            favoriteButton.setSelected(isFavorite);
            favoriteButton.setImageResource(isFavorite ?
                    R.drawable.ic_heart_filled : R.drawable.ic_heart_empty);

            // Bind other views
            TextView titleTextView = itemView.findViewById(R.id.titleTextView);
            titleTextView.setText(item.getType());

        }
    }

    @Override
    public PropertyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estates_vertical, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PropertyViewHolder holder, int position) {
        PropertyItem item = items.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}