package com.example.myapplication;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBar actionBar;
    private View view;
    public Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a=this;
        themeUtils.onActivityCreateSetTheme(a);
        setContentView(R.layout.help);

        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.help);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void closeActivity(View v) {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }
}
