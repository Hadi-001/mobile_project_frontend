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

public class SignupActivity extends AppCompatActivity {

    EditText username, email, phone, firstName, lastName, password, confirmPassword;
    Button signupButton;
    String SIGNUP_URL = "http://10.0.2.2/mobile_project_backend/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets b = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(b.left, b.top, b.right, b.bottom);
                    return insets;
                }
        );

        username   = findViewById(R.id.username);
        email      = findViewById(R.id.email);
        phone      = findViewById(R.id.phone);
        firstName  = findViewById(R.id.firstName);
        lastName   = findViewById(R.id.lastName);
        password   = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signupButton = findViewById(R.id.signupButton);

        signupButton.setOnClickListener(v -> signupUser());

        Button loginInsteadButton = findViewById(R.id.loginInsteadButton);
        loginInsteadButton.setOnClickListener(v -> {
            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void signupUser() {
        String u = username.getText().toString().trim();
        String e = email.getText().toString().trim();
        String p = phone.getText().toString().trim();
        String fn = firstName.getText().toString().trim();
        String ln = lastName.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();

        if (u.isEmpty() || e.isEmpty() || p.isEmpty() || fn.isEmpty() || ln.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Invalid email format");
            email.requestFocus();
            return;
        }

        if (!p.matches("\\d{7,15}")) {
            phone.setError("Phone number must be 7-15 digits");
            phone.requestFocus();
            return;
        }

        if (!pass.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return;
        }


        String url = SIGNUP_URL
                + "?username=" + Uri.encode(u)
                + "&email=" + Uri.encode(e)
                + "&phonenb=" + Uri.encode(p)
                + "&first_name=" + Uri.encode(fn)
                + "&last_name=" + Uri.encode(ln)
                + "&password=" + Uri.encode(pass);

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException s) {
                        s.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(req);
    }
}
