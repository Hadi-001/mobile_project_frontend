package com.example.mobile_project_frontend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyVerticalAdapter extends RecyclerView.Adapter<PropertyVerticalAdapter.ViewHolder> {

    private List<PropertyItem> itemList;
    private Context context;
    protected static final int REQUEST_CODE_PROPERTY_DETAIL = 2006;

    public PropertyVerticalAdapter(List<PropertyItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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
        PropertyItem item = itemList.get(position);

        // Bind all data to views
        holder.titleTextView.setText(item.getType());
        holder.propertyTitleTextView.setText(item.getType());
        holder.locationTextView.setText(item.getCity() + ", " + item.getStreet());
        holder.bedroomCountTextView.setText(String.valueOf(item.getBeds()));
        holder.bathroomCountTextView.setText(String.valueOf(item.getBaths()));
        holder.priceTextView.setText("$" + String.format("%,.0f", item.getPrice())); // Formats price with commas

        // Load image with Glide
        if (holder.imageView != null) {
            Glide.with(holder.itemView.getContext())
                    .load("http://10.0.2.2/" + item.getImageLink())
                    .into(holder.imageView);
        }

        // Set favorite state
        if (item.isFavorite()) {
            holder.favoriteButton.setImageResource(R.drawable.ic_heart_filled);
            holder.favoriteButton.setTag(R.drawable.ic_heart_filled);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_heart_empty);
            holder.favoriteButton.setTag(R.drawable.ic_heart_empty);
        }

        // Set up favorite button click
        User user = new User(context);
        if (user.getUserId() > 0) {
            holder.favoriteButton.setOnClickListener(v -> {
                ImageButton button = (ImageButton) v;
                Object tag = button.getTag();
                button.setEnabled(false);
                String action;

                if (tag != null && (int) tag == R.drawable.ic_heart_filled) {
                    button.setImageResource(R.drawable.ic_heart_empty);
                    button.setTag(R.drawable.ic_heart_empty);
                    item.setFavorite(false);
                    action = "remove";
                } else {
                    button.setImageResource(R.drawable.ic_heart_filled);
                    button.setTag(R.drawable.ic_heart_filled);
                    item.setFavorite(true);
                    action = "add";
                }

                updateFavoriteInDatabase(item.getEstateId(), action, button);
            });
        }

        // Set up item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyDetailActivity.class);
            intent.putExtra("estate_id", item.getEstateId());

            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).startActivityForResult(intent, REQUEST_CODE_PROPERTY_DETAIL);
            } else {
                context.startActivity(intent);
            }
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).overridePendingTransition(
                        android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void updateFavoriteInDatabase(int estateId, String action, ImageButton button) {
        String url = "http://10.0.2.2/mobile_project_backend/favorite.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    button.setEnabled(true);
                },
                error -> {
                    button.setEnabled(true);
                    error.printStackTrace();
                    Toast.makeText(context, "Error updating favorite", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                User user = new User(context);
                Map<String, String> params = new HashMap<>();
                params.put("user_id", String.valueOf(user.getUserId()));
                params.put("estate_id", String.valueOf(estateId));
                params.put("action", action);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView propertyTitleTextView;
        TextView locationTextView;
        TextView bedroomCountTextView;
        TextView bathroomCountTextView;
        TextView priceTextView;
        ImageButton favoriteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImage);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            propertyTitleTextView = itemView.findViewById(R.id.propertyTitleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            bedroomCountTextView = itemView.findViewById(R.id.bedroomCountTextView);
            bathroomCountTextView = itemView.findViewById(R.id.bathroomCountTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            favoriteButton.setFocusable(true);
            favoriteButton.setClickable(true);
        }
    }
    public void updateList(List<PropertyItem> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }
}