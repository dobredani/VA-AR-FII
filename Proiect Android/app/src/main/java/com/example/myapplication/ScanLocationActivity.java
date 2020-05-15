package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ScanLocationActivity extends AppCompatActivity implements AsyncQR {

    Intent intent;
    public static Activity a;

    @Override
    public void onScanCompleted(String result) {
        Log.i("NavClass", "Found code: " + result);

        // Hide scanner
        FragmentManager fm = getSupportFragmentManager();
        Fragment scannerFragment = fm.findFragmentById(R.id.scanner_fragment);
        if (scannerFragment.isVisible()) {
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .hide(scannerFragment)
                    .commit();
        }
        intent.putExtra("returnedData", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        a=this;
        themeUtils.onActivityCreateSetTheme(a);
        super.onCreate(savedInstanceState);
        intent = getIntent();
        System.out.println(intent.getIntExtra("test", 0));
        setContentView(R.layout.activity_scan_location);

        FragmentManager fm = getSupportFragmentManager();
        Fragment scannerFragment = fm.findFragmentById(R.id.scanner_fragment);
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .show(scannerFragment)
                .commit();

        ((ScanQR) scannerFragment).resumeScan();
    }

}