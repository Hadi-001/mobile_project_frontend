package com.example.mobile_project_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etAbout, etPhone, etEmail;
    private MaterialButton btnSave, btnMyProperties, helpBtn, logoutBtn;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName  = findViewById(R.id.etName);
        etAbout = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        btnSave = findViewById(R.id.btnSave);
        helpBtn = findViewById(R.id.helpBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        btnMyProperties = findViewById(R.id.btnMyProperties);

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, HelpSupport.class);
                startActivity(i);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(ProfileActivity.this);
                user.logout();
                Intent i = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        btnSave.setOnClickListener(v -> saveProfile());

        btnMyProperties.setOnClickListener(v ->
                startActivity(new android.content.Intent(
                        ProfileActivity.this, MyPropertiesActivity.class)));

        setupBottomNavigation();
    }

    /* -------------------------------------------------- */
    private void saveProfile() {
        User user = new User(this);
        int userId = user.getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String name  = etName .getText().toString().trim();
        String about = etAbout.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(about) &&
                TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/mobile_project_backend/update_user_profile.php";

        Volley.newRequestQueue(this).add(new StringRequest(
                Request.Method.POST, url,
                r -> {
                    Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();

                    /* 🔽  Clear inputs after success */
                    etName .setText("");
                    etAbout.setText("");
                    etPhone.setText("");
                    etEmail.setText("");
                },
                e -> Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show()) {

            @Override protected Map<String,String> getParams() {
                Map<String,String> p = new HashMap<>();
                p.put("user_id", String.valueOf(userId));
                if (!name .isEmpty()) p.put("name" , name );
                if (!about.isEmpty()) p.put("about", about);
                if (!phone.isEmpty()) p.put("phone", phone);
                if (!email.isEmpty()) p.put("email", email);
                return p;
            }
        });
    }
    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_fav);
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_explore) {
                if( new User(ProfileActivity.this).getUserId() < 1)startActivity(new Intent(this,RestrictActivity.class));
                else startActivity(new Intent(this,MyFavoriteActivity.class));
                return true;
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return id == R.id.navigation_fav;
        });
    }
}
