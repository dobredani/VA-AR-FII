package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< Updated upstream:Proiect Android/Interfata/app/src/main/java/com/example/myapplication/MainActivity.java
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
=======
import android.util.Log;
>>>>>>> Stashed changes:Proiect Android/app/src/main/java/com/example/myapplication/MainActivity.java
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.problem.Building;
import com.example.myapplication.problem.Location;
import com.example.myapplication.problem.Waypoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static ApplicationData applicationData = new ApplicationData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchActivity1= new Intent(MainActivity.this,StartNavigationActivity.class);
                startActivity(launchActivity1);
                getBuildingData();
                getBuildingList();
                getWaypoints("2", "5");
            }
        });

    }


<<<<<<< Updated upstream:Proiect Android/Interfata/app/src/main/java/com/example/myapplication/MainActivity.java
    public void getServerMessage(){
=======
    public void getBuildingList() {
>>>>>>> Stashed changes:Proiect Android/app/src/main/java/com/example/myapplication/MainActivity.java
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        final String url = "http://192.168.1.3:5000/rest/building";
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("JsonObject","Response: " + response.toString());
                        List<String> buildingList = JsonParser.parseBuildingList(response);
                        applicationData.setBuildings(buildingList);
                        Log.e("JsonObject", "Building List" + buildingList.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("JsonError","Error on get JSON request!");
                    }
                });
        requestQueue.add(jsonObjectRequest);
        }
<<<<<<< Updated upstream:Proiect Android/Interfata/app/src/main/java/com/example/myapplication/MainActivity.java
=======


    public void getBuildingData(){
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

         String url = "http://192.168.1.3:5000/building/";
//        Mock url until we implement function for picking building.
         url = url.concat("FII");
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonObject","Response: " + response.toString());
                        Building building =  JsonParser.parseBuilding(response);
                        applicationData.setCurrentBuilding(building);
                        Log.e("check", applicationData.getCurrentBuilding().getName());
//                        for ( Location location : applicationData.getCurrentBuilding().getLocations())
//                            Log.e("location", location.getName());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("JsonError","Error on get JSON request!");
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

    public void getWaypoints(String start, String destination) {
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        String url = "http://192.168.1.3:5000/route/FII?start=2&destination=5";
//        url = url.concat("start=" + start + "&" + "destination=" + destination);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JsonObject","Response: " + response.toString());

                        List<Waypoint> waypointList = JsonParser.parseRoute(response);
                        applicationData.setWaypoints(waypointList);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("JsonError","Error on get JSON request!");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
>>>>>>> Stashed changes:Proiect Android/app/src/main/java/com/example/myapplication/MainActivity.java

    }
}

