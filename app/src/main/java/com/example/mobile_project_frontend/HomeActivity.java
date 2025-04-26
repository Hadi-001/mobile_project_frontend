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

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTrending);
        List<HorizontalItem> itemList = generateFakeItems();
        HorizontalAdapter adapter = new HorizontalAdapter(itemList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);



        RecyclerView recyclerViewNearest = findViewById(R.id.recyclerViewNearest);
        List<HorizontalItem> nearestItemList = generateNearestFakeItems();
        HorizontalAdapter nearestAdapter = new HorizontalAdapter(nearestItemList);
        recyclerViewNearest.setAdapter(nearestAdapter);
        LinearLayoutManager nearestLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNearest.setLayoutManager(nearestLayoutManager);


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

    private List<HorizontalItem> generateNearestFakeItems() {
        List<HorizontalItem> itemList = new ArrayList<>();

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Nearest Apartment",
                "Downtown Studio",
                "Los Angeles, CA",
                1,
                1,
                "$900"
        ));

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Nearest Villa",
                "Cozy Villa",
                "Santa Monica, CA",
                3,
                2,
                "$2,700"
        ));

        itemList.add(new HorizontalItem(
                R.drawable.appartment_pic,
                "Nearest Condo",
                "Beachside Condo",
                "Venice Beach, CA",
                2,
                2,
                "$1,800"
        ));

        return itemList;
    }


}
