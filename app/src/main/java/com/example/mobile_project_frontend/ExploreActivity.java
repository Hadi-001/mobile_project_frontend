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



        spCity  = findViewById(R.id.city_spinner);
        spPrice = findViewById(R.id.price_spinner);
        spBeds  = findViewById(R.id.beds_spinner);
        spBaths = findViewById(R.id.baths_spinner);


    }



}
