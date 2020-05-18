package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.util.AsyncQR;

public class ScanLocationActivity extends AppCompatActivity implements AsyncQR {
    private Button flashB;
    private boolean isFlashOn = false;
    Intent intent;

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
        super.onCreate(savedInstanceState);
        //hides the action bar
        getSupportActionBar().hide();
        //hides the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intent = getIntent();
        System.out.println(intent.getIntExtra("test", 0));
        setContentView(R.layout.activity_scan_location);

        FragmentManager fm = getSupportFragmentManager();
        final Fragment scannerFragment = fm.findFragmentById(R.id.scanner_fragment);
        fm.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .show(scannerFragment)
                .commit();

        ((ScanQR) scannerFragment).resumeScan();

        flashB = findViewById(R.id.flash);
        flashB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn == false) {
                    ((ScanQR) scannerFragment).flashON();
                    isFlashOn = true;
                } else {
                    ((ScanQR) scannerFragment).flashOFF();
                    isFlashOn = false;
                }
            }
        });

    }


    public void closeActivity(View view) {
        finish();
    }


}