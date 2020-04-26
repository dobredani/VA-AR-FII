package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StartNavigationActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final ListView lv = (ListView) findViewById(R.id.listView);
        String[] locations = new String[] {
                "C112",
                "C2",
                "C403",
                "C909",
                "Exit" };

        List<String> locationsList = new ArrayList<String>(Arrays.asList(locations));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, locationsList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(Color.rgb(243,237,218));

                // Generate ListView Item using TextView
                return view;
            }
        };
        lv.setAdapter(arrayAdapter);

        ImageView searchAruco = findViewById(R.id.cameraBtn);
        searchAruco.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchActivity1 = new Intent(StartNavigationActivity.this, ScanLocationActivity.class);
                launchActivity1.putExtra("test", 1);
                startActivityForResult(launchActivity1, 1);
            }
        });

        Button openCamera = findViewById(R.id.navigationBtn);
        openCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchActivity1= new Intent(StartNavigationActivity.this, CameraActivity.class);
            }
        });

    };
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String msg = data.getStringExtra("returnedData");
            EditText editText = findViewById(R.id.currentLocation);
            editText.setText(msg);
        }
    }

}