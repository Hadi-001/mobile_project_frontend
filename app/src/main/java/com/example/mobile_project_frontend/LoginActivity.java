package com.example.mobile_project_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button loginButton;
    String LOGIN_URL = "http://10.0.2.2/mobile_project_backend/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(b.left, b.top, b.right, b.bottom);
                    return insets;
                }
        );

        username    = findViewById(R.id.username);
        password    = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());

        Button signupInsteadButton = findViewById(R.id.signupButton);
        signupInsteadButton.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void loginUser() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = LOGIN_URL
                + "?username=" + Uri.encode(user)
                + "&password=" + Uri.encode(pass);

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            String id = response.getString("message");
                            Toast.makeText(getApplicationContext(), "Login successful! User ID: " + id, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.putExtra("userId", id);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }
}