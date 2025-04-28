package com.example.mobile_project_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RestrictActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restrict);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // to hide the action bar
        }
        navView = findViewById(R.id.nav_view);
        loginBtn = findViewById(R.id.restrictedLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestrictActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, ExploreActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_fav) {

                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {

                return true;
            }
            return false;
        });


    }
}