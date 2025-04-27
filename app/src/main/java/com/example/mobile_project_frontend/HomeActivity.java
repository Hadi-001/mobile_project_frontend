package com.example.mobile_project_frontend;

import static android.app.ProgressDialog.show;

import android.os.Bundle;
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
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // to hide the action bar
        }
        navView = findViewById(R.id.nav_view);

        user = new User(this);
        user.setUserId(-1);

//        navView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    // Already in HomeActivity, do nothing
//                    return true;
//                case R.id.navigation_explore:
//                    startActivity(new Intent(this, ExploreActivity.class));
//                    return true;
//                case R.id.navigation_fav:
//                    startActivity(new Intent(this, FavoritesActivity.class));
//                    return true;
//                case R.id.navigation_profile:
//                    startActivity(new Intent(this, ProfileActivity.class));
//                    return true;
//            }
//            return false;
//        });


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
                                        "$" + estate.getDouble("price"),
                                        estate.getInt("is_liked") == 1
                                ));
                            }
                            RecyclerView recyclerView = findViewById(R.id.recyclerViewTrending);
                            HorizontalAdapter adapter = new HorizontalAdapter(itemList,HomeActivity.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                                        "$" + estate.getDouble("price"),
                                        estate.getInt("is_liked") == 1
                                ));
                            }

                            RecyclerView recyclerViewNearest = findViewById(R.id.recyclerViewNearest);
                            HorizontalAdapter adapter = new HorizontalAdapter(itemList,HomeActivity.this);
                            recyclerViewNearest.setAdapter(adapter);
                            recyclerViewNearest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
