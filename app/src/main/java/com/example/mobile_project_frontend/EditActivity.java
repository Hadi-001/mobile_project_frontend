package com.example.mobile_project_frontend;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    private int estateId;
    private EditText etPrice, etBeds, etBaths, etCity, etStreet, etDescription;

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_edit);

        estateId = getIntent().getIntExtra("estate_id",-1);

        etPrice       = findViewById(R.id.etPrice);
        etBeds        = findViewById(R.id.etBeds);
        etBaths       = findViewById(R.id.etBaths);
        etCity        = findViewById(R.id.etCity);
        etStreet      = findViewById(R.id.etStreet);
        etDescription = findViewById(R.id.etDescription);

        findViewById(R.id.btnSaveEdit).setOnClickListener(v -> save());
    }

    private void save(){
        String url="http://10.0.2.2/mobile_project_backend/update_estate.php";

        String price=etPrice.getText().toString().trim();
        String beds =etBeds.getText().toString().trim();
        String baths=etBaths.getText().toString().trim();
        String city =etCity.getText().toString().trim();
        String street=etStreet.getText().toString().trim();
        String desc =etDescription.getText().toString().trim();

        if(TextUtils.isEmpty(price) && TextUtils.isEmpty(beds) &&
                TextUtils.isEmpty(baths) && TextUtils.isEmpty(city) &&
                TextUtils.isEmpty(street)&& TextUtils.isEmpty(desc)){
            Toast.makeText(this,"Nothing to update!",Toast.LENGTH_SHORT).show();
            return;
        }

        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,url,
                        r->{ setResult(RESULT_OK); finish(); },
                        e->{ Toast.makeText(this,"Update failed",Toast.LENGTH_SHORT).show();}
                ){
                    @Override protected Map<String,String> getParams(){
                        Map<String,String> p=new HashMap<>();
                        p.put("estate_id", String.valueOf(estateId));
                        if(!price.isEmpty()) p.put("price", price);
                        if(!beds.isEmpty())  p.put("beds" , beds);
                        if(!baths.isEmpty()) p.put("baths", baths);
                        if(!city.isEmpty())  p.put("city" , city);
                        if(!street.isEmpty())p.put("street",street);
                        if(!desc.isEmpty())  p.put("description", desc);
                        return p;
                    }});
    }
}
