package com.example.mobile_project_frontend;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
// …

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText etName, etAbout, etPhone, etEmail;
    private ImageView ivAvatar;
    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // … toolbar and view binding …

        ivAvatar = findViewById(R.id.tvAvatar);
        etName   = findViewById(R.id.etName);
        etAbout  = findViewById(R.id.etPassword);
        etPhone  = findViewById(R.id.etPhone);
        etEmail  = findViewById(R.id.etEmail);
        btnSave  = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String name  = etName.getText().toString().trim();
            String about = etAbout.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            // TODO: validate and actually save to your backend or prefs

            Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
        });
    }
}
