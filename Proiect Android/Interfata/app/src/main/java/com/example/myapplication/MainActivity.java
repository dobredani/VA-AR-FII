package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AsyncQR {

    @Override
    public void onScanCompleted(String result) {
        Log.i("NavClass","Found code: " + result);

        // Do something with the code
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.startBtn);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchActivity1 = new Intent(MainActivity.this, StartNavigationActivity.class);
                startActivity(launchActivity1);
            }
        });

        if (getResources().getString(R.string.DEBUG_MODE).equalsIgnoreCase("true")){
            Button debugQR = findViewById(R.id.debug_QR);
            debugQR.setVisibility(View.VISIBLE);
            debugQR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchActivity1 = new Intent(MainActivity.this, ScanQR.class);
                    startActivity(launchActivity1);
                }
            });

            Button debugAruco = findViewById(R.id.debug_Aruco);
            debugAruco.setVisibility(View.VISIBLE);
            debugAruco.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchActivity1 = new Intent(MainActivity.this, ImageProcessing.class);
                    startActivity(launchActivity1);
                }
            });

        }
    }


    public void getServerMessage() {
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        final String url = "http://192.168.1.2:5000/";
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //do something with response
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //deal with errors
                        error.printStackTrace();
                        requestQueue.stop();
                    }
                });
        requestQueue.add(stringRequest);

        }
}


