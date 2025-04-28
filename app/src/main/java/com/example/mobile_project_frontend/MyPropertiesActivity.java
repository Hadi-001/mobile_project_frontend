package com.example.mobile_project_frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPropertiesActivity extends AppCompatActivity {

    private static final int REQ_EDIT = 101;
    User user;

    private BottomNavigationView navView;
    ImageView apartmentImage, houseImage, penthouseImage, villaImage,mansionImage;
    private RecyclerView rv;
    private MyPropertiesAdapter adapter;
    private final List<PropertyItem> data = new ArrayList<>();

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_my_properties);

        navView = findViewById(R.id.nav_view);

        user = new User(this);

        setupBottomNavigation();


        rv = findViewById(R.id.MyPropertiesRecycleView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyPropertiesAdapter(
                data,
                this,
                this::confirmDelete,
                p -> {
                    Intent it = new Intent(this, EditActivity.class);
                    it.putExtra("estate_id", p.getEstateId());
                    startActivityForResult(it, REQ_EDIT);
                });
        rv.setAdapter(adapter);

        load();
    }

    private void load() {
        int uid = new User(this).getUserId();
        String url = "http://10.0.2.2/mobile_project_backend/get_user_estates.php?owner_id="+uid;

        Volley.newRequestQueue(this).add(
                new JsonArrayRequest(Request.Method.GET,url,null,
                        this::parse,
                        e -> Toast.makeText(this,"Load failed",Toast.LENGTH_SHORT).show()));
    }

    private void parse(JSONArray arr){
        try{
            data.clear();
            for(int i=0;i<arr.length();i++){
                JSONObject o=arr.getJSONObject(i);
                data.add(new PropertyItem(
                        o.getInt("estate_id"),  o.getString("type"),
                        o.getInt("beds"),       o.getInt("baths"),
                        o.getDouble("price"),   o.getString("city"),
                        o.getString("street"),  o.getString("image_link"),
                        o.getInt("views"),      o.getDouble("area"),
                        o.getString("description"),
                        o.getString("date_built"), o.getInt("owner_id"),
                        o.getString("post_date"), false));
            }
            adapter.notifyDataSetChanged();
        }catch(Exception e){ e.printStackTrace();}
    }

    private void confirmDelete(PropertyItem p){
        new AlertDialog.Builder(this)
                .setTitle("Delete property?")
                .setMessage("Are you sure you want to delete this property?")
                .setPositiveButton("Yes",(d,w)->doDelete(p))
                .setNegativeButton("No",null).show();
    }

    private void doDelete(PropertyItem p){
        String url="http://10.0.2.2/mobile_project_backend/delete_estate.php";
        Volley.newRequestQueue(this).add(
                new StringRequest(Request.Method.POST,url,
                        r->{ data.remove(p); adapter.notifyDataSetChanged();
                            Toast.makeText(this,"Deleted!",Toast.LENGTH_SHORT).show();},
                        e->{ Toast.makeText(this,"Delete failed",Toast.LENGTH_SHORT).show();}
                ){
                    @Override protected Map<String,String> getParams(){
                        Map<String,String> m=new HashMap<>(); m.put("estate_id",p.getEstateId()+""); return m;
                    }});
    }
    private void setupBottomNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_profile);
        navView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                finish();
                return true;
            } else if (id == R.id.navigation_fav) {
                if(user.getUserId() == -1) startActivity(new Intent(this,RestrictActivity.class));
                startActivity(new Intent(this,MyFavoriteActivity.class));
                finish();
                return true;
            }else if(id == R.id.navigation_profile){
                if(user.getUserId() == -1) startActivity(new Intent(this,RestrictActivity.class));
                startActivity(new Intent(this,MyFavoriteActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
    @Override protected void onActivityResult(int r,int c,Intent d){
        super.onActivityResult(r,c,d);
        if(r==REQ_EDIT && c==RESULT_OK) load();
    }
}
