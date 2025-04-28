package com.example.mobile_project_frontend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONException;
import org.json.JSONObject;

public class PropertyDetailActivity extends AppCompatActivity {

    private int propertyId;
    private String ownerPhone, ownerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

//        MaterialToolbar toolbar = findViewById(R.id.topBar);
//        toolbar.setNavigationOnClickListener(v -> {
//            onBackPressed();
//        });

        // Get the property ID from intent
        propertyId = getIntent().getIntExtra("estate_id", -1);

        if (propertyId == -1) {
            Toast.makeText(this, "Invalid property", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load property data
        fetchPropertyDetails();

        // Handle back button click
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }


    private void fetchPropertyDetails() {
        String url = "http://10.0.2.2/mobile_project_backend/get_property_details.php?property_id=" + propertyId;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            JSONObject property = response.getJSONObject("property");
                            updateUI(property);
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void updateUI(JSONObject property) throws JSONException {
        // Update image
        ImageView itemImage = findViewById(R.id.itemImage);
        Glide.with(this)
                .load("http://10.0.2.2/" + property.getString("image_link"))
                .into(itemImage);

        // Update text views
        TextView tvType = findViewById(R.id.tvtype);
        tvType.setText(property.getString("type"));

        TextView propertyTitle = findViewById(R.id.propertyTitleTextView);
        propertyTitle.setText(property.getString("type") + " in " + property.getString("city"));

        TextView location = findViewById(R.id.locationTextView);
        location.setText(property.getString("city") + ", " + property.getString("street"));

        TextView price = findViewById(R.id.propertyPriceTextView);
        price.setText("$" + property.getString("price"));

        TextView bedrooms = findViewById(R.id.bedroomsTextView);
        bedrooms.setText(property.getInt("beds") + " Beds");

        TextView bathrooms = findViewById(R.id.bathroomsTextView);
        bathrooms.setText(property.getInt("baths") + " Baths");

        TextView area = findViewById(R.id.areaTextView);
        area.setText(property.getDouble("area") + " m²");

        TextView views = findViewById(R.id.viewsTextView);
        views.setText(property.getInt("views") + " Views");

        TextView description = findViewById(R.id.propertyDescriptionTextView);
        description.setText(property.getString("description"));

        TextView dateBuilt = findViewById(R.id.dateBuiltTextView);
        dateBuilt.setText("Built on: " + property.getString("date_built"));

        // Save owner contact info
        ownerPhone = property.getString("phone_number");
        ownerEmail = property.getString("email");

        // Set up contact buttons
        findViewById(R.id.callButton).setOnClickListener(v -> callOwner());
        findViewById(R.id.emailButton).setOnClickListener(v -> emailOwner());
        findViewById(R.id.whatsappButton).setOnClickListener(v -> whatsappOwner());
    }

    private void callOwner() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + ownerPhone));
        startActivity(intent);
    }

    private void emailOwner() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + ownerEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding your property listing");
        startActivity(intent);
    }

    private void whatsappOwner() {
        try {
            String url = "https://whatsapp.com";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}