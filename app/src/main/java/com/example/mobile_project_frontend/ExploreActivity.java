package com.example.mobile_project_frontend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

    /** Fetch every estate from backend */
    private void fetchAllEstates() {
        String url = "http://10.0.2.2/mobile_project_backend/get_all_estates.php";
        RequestQueue q = Volley.newRequestQueue(this);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    parseAndDisplay(response);
                },
                error -> error.printStackTrace()
        );
        q.add(req);
    }

    /** Parse JSON array and rebuild UI */
    private void parseAndDisplay(JSONArray arr) {
        try {
            allProperties.clear();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                allProperties.add(new PropertyItem(
                        o.getInt("estate_id"),
                        o.getString("type"),
                        o.getInt("beds"),
                        o.getInt("baths"),
                        o.getDouble("price"),
                        o.getString("city"),
                        o.getString("street"),
                        o.getString("image_link"),
                        o.getInt("views"),
                        o.getDouble("area"),
                        o.getString("description"),
                        o.getString("date_build"),
                        o.getInt("owner_id"),
                        o.getString("post_date"),
                        false
                ));
            }
            rebuildSections();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Clear and re-draw all sections & cards */
    private void rebuildSections() {
        containerLayout.removeAllViews();
        sectionHeaders.clear();

        addSection("Apartment",  filterByType("Apartment"));
        addSection("Villa",      filterByType("Villa"));
        addSection("Penthouse",  filterByType("Penthouse"));
        addSection("House",      filterByType("House"));
        addSection("Mansion",    filterByType("Mansion"));
    }

    private List<PropertyItem> filterByType(String type) {
        List<PropertyItem> sub = new ArrayList<>();
        for (PropertyItem p : allProperties) {
            if (p.getType().equalsIgnoreCase(type)) sub.add(p);
        }
        return sub;
    }

    private void addSection(String title, List<PropertyItem> items) {
        if (items.isEmpty()) return;

        TextView header = new TextView(this);
        header.setText(title);
        header.setTextSize(22);
        header.setPadding(8, 32, 8, 8);
        containerLayout.addView(header);
        sectionHeaders.add(header);

        for (PropertyItem p : items) {
            containerLayout.addView(card(p));
        }
    }

    private View card(PropertyItem item) {
        View v = LayoutInflater.from(this)
                .inflate(R.layout.estates_vertical, containerLayout, false);

        ((ImageView)v.findViewById(R.id.itemImage))
                .setImageResource(R.drawable.appartment_pic);
        ((TextView)v.findViewById(R.id.titleTextView))
                .setText(item.getType());
        ((TextView)v.findViewById(R.id.propertyTitleTextView))
                .setText(item.getStreet());
        ((TextView)v.findViewById(R.id.locationTextView))
                .setText(item.getCity());
        ((TextView)v.findViewById(R.id.bedroomCountTextView))
                .setText(String.valueOf(item.getBeds()));
        ((TextView)v.findViewById(R.id.bathroomCountTextView))
                .setText(String.valueOf(item.getBaths()));
        ((TextView)v.findViewById(R.id.priceTextView))
                .setText(String.format("$%,.0f", item.getPrice()));

        ImageButton heart = v.findViewById(R.id.favoriteButton);
        heart.setOnClickListener(b -> {
            boolean sel = !heart.isSelected();
            heart.setSelected(sel);
            heart.setImageResource(
                    sel ? R.drawable.ic_heart_filled : R.drawable.ic_heart_empty
            );
        });

        v.setTag(item);
        allCards.add(v);
        return v;
    }

    /** Apply local filters on the already-fetched list */
    private void applyFilters() {
        // read spinner values
        cityFilter = spCity.getSelectedItemPosition()==0
                ? null
                : spCity.getSelectedItem().toString();
        priceBand = spPrice.getSelectedItemPosition();
        minBeds   = parseMin(spBeds.getSelectedItem().toString());
        minBaths  = parseMin(spBaths.getSelectedItem().toString());

        // rebuild with filtering
        containerLayout.removeAllViews();
        for (String type : new String[]{"Apartment","Villa","Penthouse","House","Mansion"}) {
            List<PropertyItem> list = filterByType(type);
            // apply spinner filters
            List<PropertyItem> filtered = new ArrayList<>();
            for (PropertyItem p : list) {
                if ((selectedTypes.isEmpty() || selectedTypes.contains(p.getType()))
                        && (cityFilter==null   || p.getCity().startsWith(cityFilter))
                        && (minBeds  < 0     || p.getBeds() >= minBeds)
                        && (minBaths < 0     || p.getBaths()>= minBaths)
                        && pricePass(p.getPrice()))
                {
                    filtered.add(p);
                }
            }
            addSection(type, filtered);
        }
        disableApplyButton();
    }

    private boolean pricePass(double p) {
        switch (priceBand) {
            case 0: return true;
            case 1: return p < 2000;
            case 2: return p < 5000;
            case 3: return p < 10000;
            case 4: return p >= 10000;
            default: return true;
        }
    }

    private int parseMin(String txt) {
        return txt.startsWith("Any")
                ? -1
                : Integer.parseInt(txt.replaceAll("[^0-9]",""));
    }
}
