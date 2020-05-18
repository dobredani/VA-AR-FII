package com.example.myapplication;



import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.ThemeUtils;
import com.google.android.material.navigation.NavigationView;

public class HelpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBar actionBar;
    private View view;
    public Activity a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a=this;
        ThemeUtils.onActivityCreateSetTheme(a);
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
