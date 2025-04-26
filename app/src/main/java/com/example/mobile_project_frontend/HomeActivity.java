package com.example.mobile_project_frontend;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // to hide the action bar
        }
        navView = findViewById(R.id.nav_view);

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

        // Inside your HomeActivity.java, in the onCreate() method:

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTrending); // <-- Use the correct ID from your XML
        List<HorizontalItem> itemList = generateFakeItems(); // This is the static data method you created
        HorizontalAdapter adapter = new HorizontalAdapter(itemList);

// Set up the RecyclerView
        recyclerView.setAdapter(adapter);

// Set up the horizontal LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);


    }

    private List<HorizontalItem> generateFakeItems() {
        List<HorizontalItem> itemList = new ArrayList<>();

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Apartment",
                "Royal Apartment",
                "Los Angeles, CA",
                3,
                2,
                "$1,500"
        ));

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Villa",
                "Luxury Villa",
                "Miami, FL",
                5,
                4,
                "$3,800"
        ));

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Condo",
                "Sunset Condo",
                "San Diego, CA",
                2,
                1,
                "$2,100"
        ));

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Penthouse",
                "Skyline Penthouse",
                "New York, NY",
                4,
                3,
                "$5,500"
        ));

        return itemList;
    }

}
