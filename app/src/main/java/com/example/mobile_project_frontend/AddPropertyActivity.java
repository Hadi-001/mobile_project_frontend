package com.example.mobile_project_frontend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AddPropertyActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST_CODE = 1001;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    int cameraPermissionGranted = 0;


    int estate_id;
    String image_link;

    private Spinner propertyTypeSpinner;
    private EditText priceEditText, areaEditText, bedsEditText, bathsEditText;
    private EditText cityEditText, streetEditText, descriptionEditText;
    private EditText dateBuildEditText;
    private Button uploadImageButton,uploadImageCameraButton, submitButton;
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
        uploadImageCameraButton.setOnClickListener(v -> {
            if (cameraPermissionGranted == 1) {
                openCamera();
            } else {
                askForPermission();
            }
        });
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
        uploadImageCameraButton = findViewById(R.id.uploadImageCameraButton);
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
        String dateFormat = "yyyy-mm-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateBuildEditText.setText(sdf.format(calendar.getTime()));
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(this, "No Camera app found!", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(null);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                // Image selected from gallery
                imageUri = data.getData();
                propertyImageView.setImageURI(imageUri);
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                // Image captured from camera
                if (imageUri != null) {
                    propertyImageView.setImageURI(imageUri);
                }
            }
        }
    }


    private void submitProperty() {
        if (!validateFields()) {
            return;
        }

        uploadImageToServer(imageUri);

    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(AddPropertyActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddPropertyActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            cameraPermissionGranted = 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraPermissionGranted = 1;
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                cameraPermissionGranted = 0;
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
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
        String url = "http://10.0.2.2/mobile_project_backend/add_property.php";

        String type = propertyTypeSpinner.getSelectedItem().toString();
        String price = priceEditText.getText().toString().trim();
        String area = areaEditText.getText().toString().trim();
        String beds = bedsEditText.getText().toString().trim();
        String baths = bathsEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String street = streetEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dateBuilt = dateBuildEditText.getText().toString().trim();

        // For now, hardcode owner_id (later you can get it from login session)
        String ownerId = "" + new User(this).getUserId();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.getBoolean("success")) {
                            estate_id = jsonResponse.getInt("estate_id");
                            Toast.makeText(this, "Property " + estate_id + " added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("AddPropertyError", "Error: " + e.getMessage(), e);
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", type);
                params.put("beds", beds);
                params.put("baths", baths);
                params.put("price", price);
                params.put("city", city);
                params.put("street", street);
                params.put("area", area);
                params.put("description", description);
                params.put("date_built", "2025-10-12");
                params.put("owner_id", ownerId);
                params.put("image_link",image_link);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private void uploadImageToServer(Uri imageUri) {
        String url = "http://10.0.2.2/mobile_project_backend/upload_image.php";

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    // Handle success
                    try {
                        JSONObject jsonResponse = new JSONObject(new String(response.data));
                        if (jsonResponse.getBoolean("success")) {
                            Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                            image_link = jsonResponse.getString("image_link");
                            addPropertyToDatabase();
                            finish();
                        } else {
                            Toast.makeText(this, "Failed: " + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            Log.e("UploadImageErrorr", "Error: " + jsonResponse.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    error.printStackTrace();
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                byte[] imageData = getFileDataFromUri(imageUri);
                String imageName = "uploaded_image.png";  // You can get the file name from the URI if needed
                params.put("image", new DataPart(imageName, imageData));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
    }



    private byte[] getFileDataFromUri(Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }


}
