package com.example.mobile_project_frontend;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    ImageView apartmentImage, houseImage, penthouseImage, villaImage,mansionImage;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // to hide the action bar
        }
        navView = findViewById(R.id.nav_view);

        apartmentImage = findViewById(R.id.apartment_category);
        villaImage = findViewById(R.id.villa_category);
        penthouseImage = findViewById(R.id.penthouse_category);
        houseImage = findViewById(R.id.house_category);
        mansionImage = findViewById(R.id.mansion_category);

        user = new User(this);

        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                // Already in HomeActivity, do nothing
                return true;
            } else if (item.getItemId() == R.id.navigation_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_fav) {
                if(user.getUserId() < 1)startActivity(new Intent(this,RestrictActivity.class));
                else startActivity(new Intent(this,MyFavoriteActivity.class));
                return true;
              } else if (item.getItemId() == R.id.navigation_profile) {
                if(user.getUserId() < 1)startActivity(new Intent(this,RestrictActivity.class));
                else startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });



        apartmentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to ExploreActivity with the category as extra
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                intent.putExtra("category", "Apartment");
                startActivity(intent);
            }
        });

        villaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                intent.putExtra("category", "Villa");
                startActivity(intent);
            }
        });

        penthouseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                intent.putExtra("category", "Penthouse");
                startActivity(intent);
            }
        });

        houseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                intent.putExtra("category", "House");
                startActivity(intent);
            }
        });

        mansionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ExploreActivity.class);
                intent.putExtra("category", "Mansion");
                startActivity(intent);
            }
        });
        fetchTrendingEstates();

        fetchLatestEstates();


    }

    private void fetchTrendingEstates() {
        String url = "http://10.0.2.2/mobile_project_backend/get_trending_estate.php?user_id=" + user.getUserId();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");

                        if (success) {
                            JSONArray estatesArray = response.getJSONArray("estates");
                            List<HorizontalItem> itemList = new ArrayList<>();

                            for (int i = 0; i < estatesArray.length(); i++) {
                                JSONObject estate = estatesArray.getJSONObject(i);
                                itemList.add(new HorizontalItem(
                                        estate.getInt("estate_id"),
                                        estate.getString("image_link"),
                                        estate.getString("type"),
                                        estate.getString("type"),
                                        estate.getString("city"),
                                        estate.getInt("beds"),
                                        estate.getInt("baths"),
                                        "$" + (int)estate.getDouble("price"),
                                        estate.getInt("is_liked") == 1
                                ));
                            }
                            RecyclerView recyclerViewTrending = findViewById(R.id.recyclerViewTrending);
                            HorizontalAdapter adapter = new HorizontalAdapter(itemList,HomeActivity.this);
                            recyclerViewTrending.setAdapter(adapter);
                            recyclerViewTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }


    private void fetchLatestEstates() {
        String url = "http://10.0.2.2/mobile_project_backend/get_latest_estate.php?user_id=" + user.getUserId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONArray estatesArray = response.getJSONArray("estates");
                            List<HorizontalItem> itemList = new ArrayList<>();

                            for (int i = 0; i < estatesArray.length(); i++) {
                                JSONObject estate = estatesArray.getJSONObject(i);
                                itemList.add(new HorizontalItem(
                                        estate.getInt("estate_id"),
                                        estate.getString("image_link"),
                                        estate.getString("type"),
                                        estate.getString("type"),
                                        estate.getString("city"),
                                        estate.getInt("beds"),
                                        estate.getInt("baths"),
                                        "$" + (int)estate.getDouble("price"),
                                        estate.getInt("is_liked") == 1
                                ));
                            }

                            RecyclerView recyclerViewLatest = findViewById(R.id.recyclerViewLatest);
                            HorizontalAdapter adapter = new HorizontalAdapter(itemList,HomeActivity.this);
                            recyclerViewLatest.setAdapter(adapter);
                            recyclerViewLatest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        Volley.newRequestQueue(this).add(request);
    }

}
