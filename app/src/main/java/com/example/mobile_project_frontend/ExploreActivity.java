package com.example.mobile_project_frontend;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ExploreActivity extends AppCompatActivity {

    private BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // to hide the action bar
        }
        navView = findViewById(R.id.nav_view);


        navView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.navigation_explore) {
//            } else if (item.getItemId() == R.id.navigation_fav) {
//                startActivity(new Intent(this, FavoritesActivity.class));
//                return true;
//            } else if (item.getItemId() == R.id.navigation_profile) {
//                startActivity(new Intent(this, ProfileActivity.class));
//                return true;
            }
            return false;
        });


    }

}
