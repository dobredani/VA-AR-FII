package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

        ImageView searchAruco = findViewById(R.id.cameraBtn);
        searchAruco.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchActivity1= new Intent(StartNavigationActivity.this,ImageProcessing.class);
                startActivity(launchActivity1);
            }
        });

        /*ImageView openCamera = findViewById(R.id.cameraBtn);
        openCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent launchActivity1= new Intent(StartNavigationActivity.this, CameraActivity.class);
                startActivity(launchActivity1);
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
