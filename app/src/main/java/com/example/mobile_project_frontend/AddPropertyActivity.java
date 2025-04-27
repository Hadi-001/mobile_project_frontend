package com.example.mobile_project_frontend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;

public class AddPropertyActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Spinner propertyTypeSpinner;
    private EditText priceEditText, areaEditText, bedsEditText, bathsEditText;
    private EditText cityEditText, streetEditText, descriptionEditText;
    private EditText dateBuildEditText;
    private Button uploadImageButton, submitButton;
    private ImageView propertyImageView;

    private Uri imageUri;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        initViews();
        setupDatePicker();

        uploadImageButton.setOnClickListener(v -> openImageChooser());
        submitButton.setOnClickListener(v -> submitProperty());
    }

    private void initViews() {
        propertyTypeSpinner = findViewById(R.id.propertyTypeSpinner);
        priceEditText = findViewById(R.id.priceEditText);
        areaEditText = findViewById(R.id.areaEditText);
        bedsEditText = findViewById(R.id.bedsEditText);
        bathsEditText = findViewById(R.id.bathsEditText);
        cityEditText = findViewById(R.id.cityEditText);
        streetEditText = findViewById(R.id.streetEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateBuildEditText = findViewById(R.id.dateBuildEditText);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        submitButton = findViewById(R.id.submitButton);
        propertyImageView = findViewById(R.id.propertyImageView);

        calendar = Calendar.getInstance();
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        dateBuildEditText.setOnClickListener(v -> {
            new DatePickerDialog(
                    AddPropertyActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });
    }

    private void updateDateLabel() {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateBuildEditText.setText(sdf.format(calendar.getTime()));
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            propertyImageView.setImageURI(imageUri);
        }
    }

    private void submitProperty() {
        if (!validateFields()) {
            return;
        }

        addPropertyToDatabase();
    }

    private boolean validateFields() {
        if (propertyTypeSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Please select property type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (priceEditText.getText().toString().trim().isEmpty()) {
            priceEditText.setError("Please enter price");
            return false;
        }
        if (areaEditText.getText().toString().trim().isEmpty()) {
            areaEditText.setError("Please enter area");
            return false;
        }
        if (bedsEditText.getText().toString().trim().isEmpty()) {
            bedsEditText.setError("Please enter number of bedrooms");
            return false;
        }
        if (bathsEditText.getText().toString().trim().isEmpty()) {
            bathsEditText.setError("Please enter number of bathrooms");
            return false;
        }
        if (cityEditText.getText().toString().trim().isEmpty()) {
            cityEditText.setError("Please enter city");
            return false;
        }
        if (streetEditText.getText().toString().trim().isEmpty()) {
            streetEditText.setError("Please enter street address");
            return false;
        }
        if (dateBuildEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please select build date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addPropertyToDatabase() {
        //User user = new User(AddPropertyActivity.this);
        int ownerId = 5;
        String type = propertyTypeSpinner.getSelectedItem().toString();
        int beds = Integer.parseInt(bedsEditText.getText().toString().trim());
        int baths = Integer.parseInt(bathsEditText.getText().toString().trim());
        double price = Double.parseDouble(priceEditText.getText().toString().trim());
        String city = cityEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        double area = Double.parseDouble(areaEditText.getText().toString().trim());
        String description = descriptionEditText.getText().toString().trim();
        String dateBuilt = dateBuildEditText.getText().toString().trim();

        JSONObject propertyData = new JSONObject();
        try {
            propertyData.put("owner_id", ownerId);
            propertyData.put("type", type);
            propertyData.put("beds", beds);
            propertyData.put("baths", baths);
            propertyData.put("price", price);
            propertyData.put("city", city);
            propertyData.put("street", street);
            propertyData.put("area", area);
            propertyData.put("description", description);
            propertyData.put("date_built", dateBuilt);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating property data", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/mobile_project_backend/add_property.php";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                propertyData,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            int estateId = response.getInt("estate_id");
                            Toast.makeText(this, "Property added successfully!", Toast.LENGTH_SHORT).show();
                            uploadImage(estateId);
                            finish();
                        } else {
                            String message = response.getString("message");
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void uploadImage(int estateId) {
        if (imageUri == null) return;

        String uploadUrl = "http://10.0.2.2/mobile_project_backend/upload_image.php";

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                uploadUrl,
                response -> {
                    Toast.makeText(AddPropertyActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(AddPropertyActivity.this, "Image upload failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("estate_id", String.valueOf(estateId));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] imageBytes = baos.toByteArray();

                    params.put("image", new DataPart(generateImageName(), imageBytes, "image/jpeg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };

        Volley.newRequestQueue(this).add(multipartRequest);
    }

    private String generateImageName() {
        return "property_" + System.currentTimeMillis() + ".jpg";
    }
}
