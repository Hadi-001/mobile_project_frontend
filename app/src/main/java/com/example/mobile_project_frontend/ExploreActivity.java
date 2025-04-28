package com.example.mobile_project_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExploreActivity extends AppCompatActivity {

    // UI Components
    private Button btnApply;
    private Spinner spCity, spPrice, spBeds, spBaths;
    private CheckBox checkApartment, checkVilla, checkPenthouse, checkHouse, checkMansion;
    private TextView emptyView;
    private RecyclerView recyclerView;

    // Data
    private final List<PropertyItem> allProperties = new ArrayList<>();
    private PropertyVerticalAdapter adapter;

    // Filters
    private final Set<String> selectedTypes = new HashSet<>();
    private String cityFilter = null;
    private int minBeds = -1, minBaths = -1, priceBand = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        initializeUI();
        setupBottomNavigation();
        fetchAllEstates();
    }

    private void initializeUI() {
        initializeViews();
        setupCheckboxes();
        setupSpinners();
        setupRecyclerView();
    }

    private void initializeViews() {
        btnApply = findViewById(R.id.btn_apply_filters);
        checkApartment = findViewById(R.id.check_apartment);
        checkVilla = findViewById(R.id.check_villa);
        checkPenthouse = findViewById(R.id.check_penthouse);
        checkHouse = findViewById(R.id.check_house);
        checkMansion = findViewById(R.id.check_mansion);
        emptyView = findViewById(R.id.emptyView);
        recyclerView = findViewById(R.id.recyclerView);

        spCity = findViewById(R.id.city_spinner);
        spPrice = findViewById(R.id.price_spinner);
        spBeds = findViewById(R.id.beds_spinner);
        spBaths = findViewById(R.id.baths_spinner);
    }

    private void setupCheckboxes() {
        // Set up checkbox listeners
        checkApartment.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedTypes("apartment", isChecked);
            enableApplyButton();
        });

        checkVilla.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedTypes("villa", isChecked);
            enableApplyButton();
        });

        checkPenthouse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedTypes("penthouse", isChecked);
            enableApplyButton();
        });

        checkHouse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedTypes("house", isChecked);
            enableApplyButton();
        });

        checkMansion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateSelectedTypes("mansion", isChecked);
            enableApplyButton();
        });

        btnApply.setOnClickListener(v -> applyFilters());
        disableApplyButton();
    }

    private void updateSelectedTypes(String type, boolean isChecked) {
        if (isChecked) {
            selectedTypes.add(type);
        } else {
            selectedTypes.remove(type);
        }
    }

    private void setupSpinners() {
        setupSpinner(spCity, R.array.city_list);
        setupSpinner(spPrice, R.array.price_list);
        setupSpinner(spBeds, R.array.beds_list);
        setupSpinner(spBaths, R.array.baths_list);
    }

    private void setupSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean firstSelection = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (firstSelection) {
                    firstSelection = false;
                    return;
                }
                enableApplyButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PropertyVerticalAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void enableApplyButton() {
        btnApply.setEnabled(true);
    }

    private void disableApplyButton() {
        btnApply.setEnabled(false);
    }

    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_explore);
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            }
            return id == R.id.navigation_explore;
        });
    }

    private void fetchAllEstates() {
        String url = "http://10.0.2.2/mobile_project_backend/get_all_estates.php";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccessResponse,
                this::handleErrorResponse
        );

        queue.add(request);
    }

    private void handleSuccessResponse(JSONArray response) {
        try {
            allProperties.clear();
            for (int i = 0; i < response.length(); i++) {
                JSONObject propertyJson = response.getJSONObject(i);
                PropertyItem property = parsePropertyItem(propertyJson);
                allProperties.add(property);
            }
            updateUIAfterFetch();
        } catch (JSONException e) {
            handleJsonError(e);
        }
    }

    private PropertyItem parsePropertyItem(JSONObject json) throws JSONException {
        return new PropertyItem(
                json.getInt("estate_id"),
                json.getString("type"),
                json.getInt("beds"),
                json.getInt("baths"),
                json.getDouble("price"),
                json.getString("city"),
                json.getString("street"),
                json.getString("image_link"),
                json.getInt("views"),
                json.getDouble("area"),
                json.getString("description"),
                json.getString("date_build"),
                json.getInt("owner_id"),
                json.getString("post_date"),
                false
        );
    }

    private void updateUIAfterFetch() {
        adapter.updateList(allProperties);
        if (allProperties.isEmpty()) {
            showEmptyState("No properties found");
        } else {
            hideEmptyState();
        }
    }

    private void handleErrorResponse(VolleyError error) {
        Log.e("ExploreActivity", "Volley error: " + error.getMessage());
        showEmptyState("Failed to load properties");
        Toast.makeText(this, "Network error occurred", Toast.LENGTH_SHORT).show();
    }

    private void handleJsonError(JSONException e) {
        Log.e("ExploreActivity", "JSON parsing error", e);
        showEmptyState("Data format error");
        Toast.makeText(this, "Error parsing property data", Toast.LENGTH_SHORT).show();
    }

    private void showEmptyState(String message) {
        emptyView.setText(message);
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void hideEmptyState() {
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void applyFilters() {
        updateFilterValues();
        List<PropertyItem> filteredList = filterProperties();
        updateUIAfterFiltering(filteredList);
    }

    private void updateFilterValues() {
        cityFilter = spCity.getSelectedItemPosition() == 0 ? null : spCity.getSelectedItem().toString();
        priceBand = spPrice.getSelectedItemPosition();
        minBeds = parseMinValue(spBeds.getSelectedItem().toString());
        minBaths = parseMinValue(spBaths.getSelectedItem().toString());
    }

    private int parseMinValue(String text) {
        return text.startsWith("Any") ? -1 : Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    private List<PropertyItem> filterProperties() {
        List<PropertyItem> filteredList = new ArrayList<>();

        if (allProperties.isEmpty()) {
            return filteredList;
        }

        for (PropertyItem property : allProperties) {
            if (matchesFilters(property)) {
                filteredList.add(property);
            }
        }
        return filteredList;
    }

    private boolean matchesFilters(PropertyItem property) {
        // Type filter - show all if none selected, or only selected types
        boolean typeMatches = selectedTypes.isEmpty() || selectedTypes.contains(property.getType());

        // City filter
        boolean cityMatches = cityFilter == null ||
                (property.getCity() != null &&
                        property.getCity().equalsIgnoreCase(cityFilter));

        // Numeric filters
        boolean bedsMatch = minBeds < 0 || property.getBeds() >= minBeds;
        boolean bathsMatch = minBaths < 0 || property.getBaths() >= minBaths;
        boolean priceMatches = matchesPriceFilter(property.getPrice());

        return typeMatches && cityMatches && bedsMatch && bathsMatch && priceMatches;
    }

    private boolean matchesPriceFilter(double price) {
        switch (priceBand) {
            case 1: return price < 2000;
            case 2: return price < 5000;
            case 3: return price < 10000;
            case 4: return price >= 10000;
            default: return true; // case 0 or any other - show all
        }
    }

    private void updateUIAfterFiltering(List<PropertyItem> filteredList) {
        adapter.updateList(filteredList);
        disableApplyButton();

        if (filteredList.isEmpty()) {
            showEmptyState("No matching properties");
        } else {
            hideEmptyState();
        }
    }
}