package com.example.mobile_project_frontend;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPropertiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyPropertiesAdapter adapter;
    private List<PropertyItem> propertyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_properties);

        recyclerView = findViewById(R.id.MyPropertiesRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        propertyList = new ArrayList<>();
        adapter = new MyPropertiesAdapter(propertyList, this, this::confirmDeleteProperty);
        recyclerView.setAdapter(adapter);

        loadMyProperties();
    }

    private void loadMyProperties() {
        User user = new User(this);
        int userId = user.getUserId();

        if (userId == -1) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/mobile_project_backend/get_user_estates.php?owner_id=" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> parseEstateResponse(response),
                error -> Toast.makeText(this, "Failed to load properties", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    private void parseEstateResponse(JSONArray response) {
        try {
            propertyList.clear();

            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = response.getJSONObject(i);

                PropertyItem item = new PropertyItem(
                        obj.getInt("estate_id"),
                        obj.getString("type"),
                        obj.getInt("beds"),
                        obj.getInt("baths"),
                        obj.getDouble("price"),
                        obj.getString("city"),
                        obj.getString("street"),
                        obj.getString("image_link"),
                        obj.getInt("views"),
                        obj.getDouble("area"),
                        obj.getString("description"),
                        obj.getString("date_build"),
                        obj.getInt("owner_id"),
                        obj.getString("post_date"),
                        false
                );

                propertyList.add(item);
            }

            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
        }
    }

    /** Show a confirmation dialog */
    private void confirmDeleteProperty(PropertyItem property) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Property")
                .setMessage("Are you sure you want to delete this property?")
                .setPositiveButton("Yes", (dialog, which) -> deleteProperty(property))
                .setNegativeButton("No", null)
                .show();
    }

    /** If confirmed, delete */
    private void deleteProperty(PropertyItem property) {
        String url = "http://10.0.2.2/mobile_project_backend/delete_estate.php";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    propertyList.remove(property);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Property deleted!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(this, "Failed to delete property", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("estate_id", String.valueOf(property.getEstateId()));
                return params;
            }
        };

        queue.add(request);
    }
}