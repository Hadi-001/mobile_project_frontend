package com.example.mobile_project_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etName, etAbout, etPhone, etEmail;
    private ImageView ivAvatar;
    private MaterialButton btnSave, btnMyProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivAvatar = findViewById(R.id.tvAvatar);
        etName   = findViewById(R.id.etName);
        etAbout  = findViewById(R.id.etPassword);
        etPhone  = findViewById(R.id.etPhone);
        etEmail  = findViewById(R.id.etEmail);
        btnSave  = findViewById(R.id.btnSave);
        btnMyProperties = findViewById(R.id.btnMyProperties);

        btnSave.setOnClickListener(v -> {
            String name  = etName.getText().toString().trim();
            String about = etAbout.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            // TODO: Save profile changes to backend or shared preferences.

            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
        });

        btnMyProperties.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyPropertiesActivity.class);
            startActivity(intent);
        });
    }
}
