package com.example.mobile_project_frontend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    private List<HorizontalItem> itemList;
    Context context;

    public HorizontalAdapter(List<HorizontalItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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
        holder.titleTextView.setText(item.getTitle());
        holder.propertyTitleTextView.setText(item.getPropertyTitle());
        holder.locationTextView.setText(item.getLocation());
        holder.bedroomCountTextView.setText(String.valueOf(item.getBedroomCount()));
        holder.bathroomCountTextView.setText(String.valueOf(item.getBathroomCount()));
        holder.priceTextView.setText(item.getPrice());

        Glide.with(holder.itemView.getContext())
                .load("http://10.0.2.2/" + item.getImageURL())
                .into(holder.imageView);


        if (item.isLiked()) {
            holder.favoriteButton.setImageResource(R.drawable.ic_heart_filled);
            holder.favoriteButton.setTag(R.drawable.ic_heart_filled);
        } else {
            holder.favoriteButton.setImageResource(R.drawable.ic_heart_empty);
            holder.favoriteButton.setTag(R.drawable.ic_heart_empty);
        }

        // like button click
        User user = new User(context);

        if (user.getUserId() > 0) {
            holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageButton button = (ImageButton) v;
                    Object tag = button.getTag();
                    button.setEnabled(false);
                    String action;

                    if (tag != null && (int) tag == R.drawable.ic_heart_filled) {
                        button.setImageResource(R.drawable.ic_heart_empty);
                        button.setTag(R.drawable.ic_heart_empty);
                        item.setLiked(false);
                        action = "remove";
                    } else {
                        button.setImageResource(R.drawable.ic_heart_filled);
                        button.setTag(R.drawable.ic_heart_filled);
                        item.setLiked(true);
                        action = "add";
                    }

                    updateFavoriteInDatabase(item.getItemId(), action, button);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != holder.favoriteButton.getId()) {
                    Intent intent = new Intent(context, PropertyDetailActivity.class);
                    intent.putExtra("estate_id", item.getItemId());
                    intent.putExtra("title", item.getTitle());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }
    private void updateFavoriteInDatabase(int estateId, String action,ImageButton button) {
        String url = "http://10.0.2.2/mobile_project_backend/favorite.php";
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                   button.setEnabled(true);
                },
                error -> {
                    // Error occurred
                    button.setEnabled(true);
                    error.printStackTrace();
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

            favoriteButton.setFocusable(true);
            favoriteButton.setClickable(true);
        }
    }
}
