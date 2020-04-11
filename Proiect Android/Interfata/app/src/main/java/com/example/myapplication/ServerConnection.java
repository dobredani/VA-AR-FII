package com.example.myapplication;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ServerConnection {
    //    The URL is different for every computer.
    //    For emulator use //10.0.0.2:5000
    //    For device, use command "ipconfig" in cmd and get IPv4 address.
    final String url = "http://192.168.1.2:5000/";
    String getResponse;

    public ServerConnection() {

    }

    public String sendRequest(Activity activity){
        final RequestQueue requestQueue = Volley.newRequestQueue(activity);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getResponse = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getResponse = "Error on get request!";
                error.printStackTrace();
                requestQueue.stop();
            }
        });
        requestQueue.add(stringRequest);
        return getResponse;
    }

}