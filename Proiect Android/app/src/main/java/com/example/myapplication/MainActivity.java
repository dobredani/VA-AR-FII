package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.problem.Building;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Dialog errorDialog;
    final static ApplicationData applicationData = new ApplicationData();
    Button start;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorDialog = new Dialog(this);

        start = findViewById(R.id.startBtn);
        progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        start.setVisibility(View.GONE);
        getBuildingData("FII");
        getBuildingList();


        Button start = findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity1 = new Intent(MainActivity.this, StartNavigationActivity.class);
                startActivity(launchActivity1);
            }
        });
    }


    public void getBuildingList() {
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        final String url = "http://192.168.0.147:5000/rest/building";

        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonObject", "Response: " + response.toString());
                        List<String> buildingList = JsonParser.parseBuildingList(response);
                        applicationData.setBuildings(buildingList);
                        Log.d("JsonObject", "Building List" + buildingList.toString());

                        List<Building> allBuildings = new ArrayList<>();
                      
                        for (String buildingName:applicationData.getBuildings())
                            if (!buildingName.equals("FII"))
                                getBuildingData(buildingName);
                       // applicationData.setCurrentBuilding(allBuildings.get(0));
                      
                        start.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showPopup();
                        Log.e("JsonError", "Error on get JSON request!");
                        start.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }


    public void getBuildingData(final String buildingName) {
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        String url = "http://192.168.0.147:5000/building/";
      
        url = url.concat(buildingName);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JsonObject", "Response: " + response.toString());
                        Building building = JsonParser.parseBuilding(response);
                        applicationData.getBuildingsData().add(building);
                        if (buildingName.equals("FII"))
                            applicationData.setCurrentBuilding(building);
                        start.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        //Log.e("check", applicationData.getCurrentBuilding().getName());
//                        for ( Location location : applicationData.getCurrentBuilding().getLocations())
//                            Log.e("location", location.getName());
                        //System.out.println(applicationData.getCurrentBuilding().getName());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showPopup();
                        Log.e("JsonError", "Error on get JSON request!");
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    public void showPopup() {
        errorDialog.setContentView(R.layout.error_popup);
        LinearLayout popup = (LinearLayout) errorDialog.findViewById(R.id.errorInfo);
        popup.setVisibility(View.VISIBLE);
        Button closePopup = (Button) errorDialog.findViewById(R.id.refreshBtn);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                refreshActivity();
            }
        });
        errorDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        errorDialog.show();
    }
}