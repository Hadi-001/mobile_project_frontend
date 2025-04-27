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
import com.google.android.material.imageview.ShapeableImageView;
import java.util.List;

public class MyPropertiesAdapter extends RecyclerView.Adapter<MyPropertiesAdapter.ViewHolder> {

    public interface OnDeleteClickListener { void onDelete(PropertyItem p); }
    public interface OnEditClickListener { void onEdit(PropertyItem p); }

    private final List<PropertyItem> propertyList;
    private final Context context;
    private final OnDeleteClickListener deleteListener;
    private final OnEditClickListener editListener;

    public MyPropertiesAdapter(List<PropertyItem> propertyList,
                               Context context,
                               OnDeleteClickListener deleteListener,
                               OnEditClickListener editListener) {
        this.propertyList = propertyList;
        this.context = context;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.estates_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyItem property = propertyList.get(position);

        // Bind all property data to views
        holder.titleTextView.setText(property.getType());
        holder.propertyTitleTextView.setText(property.getType());
        holder.locationTextView.setText(property.getCity() + ", " + property.getStreet());
        holder.bedroomCountTextView.setText(String.valueOf(property.getBeds()));
        holder.bathroomCountTextView.setText(String.valueOf(property.getBaths()));
        holder.priceTextView.setText("$" + String.format("%,.0f", property.getPrice()));

        // Load image with Glide
        Glide.with(context)
                .load("http://10.0.2.2/" + property.getImageLink())
                .into(holder.imageView);

        // Set up delete button
        holder.deleteButton.setImageResource(R.drawable.ic_delete);
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(property));

        // Set up edit click on the whole item
        holder.itemView.setOnClickListener(v -> editListener.onEdit(property));
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, propertyTitleTextView, locationTextView, bedroomCountTextView, bathroomCountTextView, priceTextView;
        ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            propertyTitleTextView = itemView.findViewById(R.id.propertyTitleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            bedroomCountTextView = itemView.findViewById(R.id.bedroomCountTextView);
            bathroomCountTextView = itemView.findViewById(R.id.bathroomCountTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            deleteButton = itemView.findViewById(R.id.favoriteButton); // Reusing the favorite button for delete
        }
    }
}