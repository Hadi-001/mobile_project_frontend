package com.example.mobile_project_frontend;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

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

        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
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
            }
            return false;
        });
    }
}
