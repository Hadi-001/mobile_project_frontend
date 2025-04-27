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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExploreActivity extends AppCompatActivity {

    private LinearLayout containerLayout;
    private Button btnApply;

    private Spinner spCity, spPrice, spBeds, spBaths;
    private Button btnApartment, btnVilla, btnPenthouse, btnHouse, btnMansion;
    private final Set<String> selectedTypes = new HashSet<>();

    private final List<View> allCards = new ArrayList<>();
    private final List<TextView> sectionHeaders = new ArrayList<>();

    private String cityFilter = null;
    private int minBeds = -1;
    private int minBaths = -1;
    private int priceBand = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        containerLayout = findViewById(R.id.containerLayout);
        btnApply = findViewById(R.id.btn_apply_filters);

        btnApartment = findViewById(R.id.filter_apartment);
        btnVilla = findViewById(R.id.filter_villa);
        btnPenthouse = findViewById(R.id.filter_penthouse);
        btnHouse = findViewById(R.id.filter_house);
        btnMansion = findViewById(R.id.filter_mansion);

        setCategoryButton(btnApartment, "Apartment");
        setCategoryButton(btnVilla, "Villa");
        setCategoryButton(btnPenthouse, "Penthouse");
        setCategoryButton(btnHouse, "House");
        setCategoryButton(btnMansion, "Mansion");

        spCity = findViewById(R.id.city_spinner);
        spPrice = findViewById(R.id.price_spinner);
        spBeds = findViewById(R.id.beds_spinner);
        spBaths = findViewById(R.id.baths_spinner);

        initSpinner(spCity, R.array.city_list);
        initSpinner(spPrice, R.array.price_list);
        initSpinner(spBeds, R.array.beds_list);
        initSpinner(spBaths, R.array.baths_list);

        btnApply.setOnClickListener(v -> applyFilters());
        disableApplyButton();

        buildStaticCards();
        setupBottomNavigation(); // <- 🔥 important
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
        b.setTextColor(Color.parseColor("#6750A4")); // your original purple text color
    }

    private void initSpinner(Spinner sp, int arrayRes) {
        ArrayAdapter<CharSequence> ad = ArrayAdapter.createFromResource(
                this, arrayRes, android.R.layout.simple_spinner_item);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(ad);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
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

    private void buildStaticCards() {
        addSection("Apartment",
                card("Apartment", "Royal Apartment", "Los Angeles, CA", 2, 2, 1500),
                card("Apartment", "Sunny Apartment", "Miami, FL", 3, 2, 2000));

        addSection("Villa",
                card("Villa", "Luxury Villa", "Dubai", 5, 4, 8000),
                card("Villa", "Beachfront Villa", "Malibu", 4, 3, 6000));

        addSection("Penthouse",
                card("Penthouse", "Skyline Penthouse", "New York, NY", 3, 3, 12000));

        addSection("House",
                card("House", "Family House", "Austin, TX", 4, 3, 4000));

        addSection("Mansion",
                card("Mansion", "Grand Mansion", "Beverly Hills", 8, 7, 25000));
    }

    private void addSection(String name, View... cards) {
        TextView header = new TextView(this);
        header.setText(name);
        header.setTextSize(22);
        header.setPadding(8, 32, 8, 8);
        containerLayout.addView(header);
        sectionHeaders.add(header);

        for (View c : cards) containerLayout.addView(c);
    }

    private View card(String type, String title, String city, int beds, int baths, int price) {
        View v = LayoutInflater.from(this).inflate(R.layout.estates_vertical, containerLayout, false);

        ((ImageView) v.findViewById(R.id.itemImage)).setImageResource(R.drawable.appartment_pic);
        ((TextView) v.findViewById(R.id.titleTextView)).setText(type);
        ((TextView) v.findViewById(R.id.propertyTitleTextView)).setText(title);
        ((TextView) v.findViewById(R.id.locationTextView)).setText(city);
        ((TextView) v.findViewById(R.id.bedroomCountTextView)).setText(String.valueOf(beds));
        ((TextView) v.findViewById(R.id.bathroomCountTextView)).setText(String.valueOf(baths));
        ((TextView) v.findViewById(R.id.priceTextView))
                .setText(String.format("$%,d", price));

        ImageButton heart = v.findViewById(R.id.favoriteButton);
        heart.setOnClickListener(b -> {
            boolean sel = !heart.isSelected();
            heart.setSelected(sel);
            heart.setImageResource(sel ? R.drawable.ic_heart_filled : R.drawable.ic_heart_empty);
        });

        v.setTag(new CardMeta(type, city, beds, baths, price));
        allCards.add(v);
        return v;
    }

    private void applyFilters() {
        cityFilter = (spCity.getSelectedItemPosition() == 0) ? null : spCity.getSelectedItem().toString();
        priceBand = spPrice.getSelectedItemPosition();
        minBeds = parseMin(spBeds.getSelectedItem().toString());
        minBaths = parseMin(spBaths.getSelectedItem().toString());

        for (View v : allCards) {
            CardMeta m = (CardMeta) v.getTag();
            boolean show =
                    (selectedTypes.isEmpty() || selectedTypes.contains(m.type)) &&
                            (cityFilter == null || m.city.startsWith(cityFilter)) &&
                            (minBeds == -1 || m.beds >= minBeds) &&
                            (minBaths == -1 || m.baths >= minBaths) &&
                            pricePass(m.price);
            v.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        refreshSectionHeaders();
        disableApplyButton();
    }

    private void refreshSectionHeaders() {
        for (TextView header : sectionHeaders) {
            boolean anyVisible = false;
            int start = containerLayout.indexOfChild(header) + 1;
            for (int i = start; i < containerLayout.getChildCount(); i++) {
                View c = containerLayout.getChildAt(i);
                if (c instanceof TextView) break;
                if (c.getVisibility() == View.VISIBLE) {
                    anyVisible = true;
                    break;
                }
            }
            header.setVisibility(anyVisible ? View.VISIBLE : View.GONE);
        }
    }

    private boolean pricePass(int p) {
        switch (priceBand) {
            case 0: return true;
            case 1: return p < 2000;
            case 2: return p >= 2000 && p < 5000;
            case 3: return p >= 5000 && p < 10000;
            case 4: return p >= 10000;
            default: return true;
        }
    }

    private int parseMin(String txt) {
        return txt.startsWith("Any") ? -1 : Integer.parseInt(txt.replaceAll("[^0-9]", ""));
    }

    static class CardMeta {
        String type, city; int beds, baths, price;
        CardMeta(String t, String c, int b, int ba, int p) {
            type = t; city = c; beds = b; baths = ba; price = p;
        }
    }

    /* ✅ setup bottom navigation */
    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_explore);

        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_explore) {
                return true; // already here
            }
//            else if (id == R.id.navigation_fav) {
//                startActivity(new Intent(this, FavoritesActivity.class));
//                finish();
//               return true;
//            } else if (id == R.id.navigation_profile) {
//                startActivity(new Intent(this, ProfileActivity.class));
//               finish();
//                return true;
//            }
            return false;
        });
    }
}
