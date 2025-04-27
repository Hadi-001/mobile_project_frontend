package com.example.mobile_project_frontend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExploreActivity extends AppCompatActivity {

    private LinearLayout containerLayout;
    private Button btnApply;

    private Spinner spCity, spPrice, spBeds, spBaths;
    private Button btnApartment, btnVilla, btnPenthouse, btnHouse, btnMansion;

    // all fetched properties
    private final List<PropertyItem> allProperties = new ArrayList<>();

    // for grouping / headers
    private final List<TextView> sectionHeaders = new ArrayList<>();


    private final List<View> allCards = new ArrayList<>();
    // filters
    private final Set<String> selectedTypes = new HashSet<>();
    private String cityFilter = null;
    private int minBeds = -1, minBaths = -1, priceBand = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        containerLayout = findViewById(R.id.containerLayout);
        btnApply        = findViewById(R.id.btn_apply_filters);

        btnApartment  = findViewById(R.id.filter_apartment);
        btnVilla      = findViewById(R.id.filter_villa);
        btnPenthouse  = findViewById(R.id.filter_penthouse);
        btnHouse      = findViewById(R.id.filter_house);
        btnMansion    = findViewById(R.id.filter_mansion);

        setCategoryButton(btnApartment,  "Apartment");
        setCategoryButton(btnVilla,      "Villa");
        setCategoryButton(btnPenthouse,  "Penthouse");
        setCategoryButton(btnHouse,      "House");
        setCategoryButton(btnMansion,    "Mansion");

        spCity  = findViewById(R.id.city_spinner);
        spPrice = findViewById(R.id.price_spinner);
        spBeds  = findViewById(R.id.beds_spinner);
        spBaths = findViewById(R.id.baths_spinner);

        initSpinner(spCity,  R.array.city_list);
        initSpinner(spPrice, R.array.price_list);
        initSpinner(spBeds,  R.array.beds_list);
        initSpinner(spBaths, R.array.baths_list);

        btnApply.setOnClickListener(v -> applyFilters());
        disableApplyButton();

        setupBottomNavigation();
        fetchAllEstates();
    }

    private void setCategoryButton(Button b, String type) {
        b.setOnClickListener(v -> {
            if (selectedTypes.contains(type)) {
                selectedTypes.remove(type);
                unHighlightButton(b);
            } else {
                selectedTypes.add(type);
                highlightButton(b);
            }
            enableApplyButton();
        });
    }

    private void highlightButton(Button b) {
        b.setTypeface(null, Typeface.BOLD);
        b.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.teal_700));
        b.setTextColor(Color.WHITE);
    }

    private void unHighlightButton(Button b) {
        b.setTypeface(null, Typeface.NORMAL);
        b.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.transparent));
        b.setTextColor(Color.parseColor("#6750A4"));
    }

    private void initSpinner(Spinner sp, int arrayRes) {
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                this, arrayRes, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if (first) { first = false; return; }
                enableApplyButton();
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });
    }

    private void enableApplyButton() {
        btnApply.setEnabled(true);
        btnApply.setAlpha(1f);
    }

    private void disableApplyButton() {
        btnApply.setEnabled(false);
        btnApply.setAlpha(0.4f);
    }

    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_explore);
        User user = new User(this);
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            }else if(id == R.id.navigation_fav){
                if(user.getUserId() == -1)startActivity(new Intent(this,MyFavoriteActivity.class));
                return true;
            }
            return id == R.id.navigation_explore;
        });
    }

    private void fetchAllEstates() {
        String url = "http://10.0.2.2/mobile_project_backend/get_all_estates.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    allProperties.clear(); // Clear any previous data

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Create PropertyItem from JSON
                            PropertyItem propertyItem = new PropertyItem(
                                    jsonObject.getInt("estate_id"),
                                    jsonObject.getString("type"),
                                    jsonObject.getInt("beds"),
                                    jsonObject.getInt("baths"),
                                    jsonObject.getDouble("price"),
                                    jsonObject.getString("city"),
                                    jsonObject.getString("street"),
                                    jsonObject.getString("image_link"),
                                    jsonObject.getInt("views"),
                                    jsonObject.getDouble("area"),
                                    jsonObject.getString("description"),
                                    jsonObject.getString("date_build"),
                                    jsonObject.getInt("owner_id"),
                                    jsonObject.getString("post_date"),
                                    jsonObject.getBoolean("is_favorite")
                            );
                            allProperties.add(propertyItem); // Add the property item to the list
                        }
                        Log.d("data",allProperties.toString());
                        // After fetching, display properties in the RecyclerView
                        PropertyVerticalAdapter adapter = new PropertyVerticalAdapter(allProperties, this);
                        RecyclerView recyclerView = findViewById(R.id.propertyRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error fetching properties", Toast.LENGTH_SHORT).show();
                });

        queue.add(jsonArrayRequest);
    }

    private void applyFilters() {
        List<PropertyItem> filteredProperties = new ArrayList<>();

        // Filter properties based on selected criteria
        for (PropertyItem property : allProperties) {
            boolean matches = true;

            // Check city filter
            if (cityFilter != null && !property.getCity().equals(cityFilter)) {
                matches = false;
            }

            // Check bed filter
            if (minBeds != -1 && property.getBeds() < minBeds) {
                matches = false;
            }

            // Check bath filter
            if (minBaths != -1 && property.getBaths() < minBaths) {
                matches = false;
            }

            // Check price filter
            if (priceBand != -1 && property.getPrice() > priceBand) {
                matches = false;
            }

            // Check type filter
            if (!selectedTypes.isEmpty() && !selectedTypes.contains(property.getType())) {
                matches = false;
            }

            // If matches all filters, add to filtered list
            if (matches) {
                filteredProperties.add(property);
            }
        }

        // Update the RecyclerView with filtered properties
        PropertyVerticalAdapter adapter = new PropertyVerticalAdapter(filteredProperties, this);
        RecyclerView recyclerView = findViewById(R.id.propertyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}
