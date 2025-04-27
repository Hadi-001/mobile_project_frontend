package com.example.mobile_project_frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private PropertyVerticalAdapter adapter;
    private List<PropertyItem> favoriteProperties;
    private RequestQueue requestQueue;
    private User user;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        // Initialize views
        favoritesRecyclerView = findViewById(R.id.MyFavoritesRecycleView);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize list and adapter
        favoriteProperties = new ArrayList<>();
        adapter = new PropertyVerticalAdapter(favoriteProperties, this);
        favoritesRecyclerView.setAdapter(adapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Get current user
        user = new User(this);

        // Load favorite properties
        loadFavoriteProperties();
    }

    private void loadFavoriteProperties() {
        if (user.getUserId() <= 0) {
            Toast.makeText(this, "Please login to view favorites", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String url = "http://10.0.2.2/mobile_project_backend/get_favorites.php?user_id=" + user.getUserId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray favoritesArray = response.getJSONArray("favorites");
                                favoriteProperties.clear();

                                for (int i = 0; i < favoritesArray.length(); i++) {
                                    JSONObject propertyJson = favoritesArray.getJSONObject(i);

                                    PropertyItem property = new PropertyItem(
                                            propertyJson.getInt("estate_id"),
                                            propertyJson.getString("type"),
                                            propertyJson.getInt("beds"),
                                            propertyJson.getInt("baths"),
                                            propertyJson.getDouble("price"),
                                            propertyJson.getString("city"),
                                            propertyJson.getString("street"),
                                            propertyJson.getString("image_link"),
                                            0, // views
                                            propertyJson.getDouble("area"),
                                            propertyJson.getString("description"),
                                            propertyJson.getString("date_built"),
                                            0, // owner_id
                                            "", // post_date
                                            true // isFavorite
                                    );

                                    favoriteProperties.add(property);
                                }

                                if (favoriteProperties.isEmpty()) {
                                    emptyView.setVisibility(View.VISIBLE);
                                    favoritesRecyclerView.setVisibility(View.GONE);
                                } else {
                                    emptyView.setVisibility(View.GONE);
                                    favoritesRecyclerView.setVisibility(View.VISIBLE);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                String message = response.getString("message");
                                Toast.makeText(MyFavoriteActivity.this, message, Toast.LENGTH_SHORT).show();
                                emptyView.setVisibility(View.VISIBLE);
                                favoritesRecyclerView.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyFavoriteActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MyFavoriteActivity.this, "Error loading favorites", Toast.LENGTH_SHORT).show();
                        emptyView.setVisibility(View.VISIBLE);
                        favoritesRecyclerView.setVisibility(View.GONE);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh favorites when returning to this activity
        loadFavoriteProperties();
    }
}