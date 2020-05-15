package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class NavigationActivity extends CameraActivity {
    public static Activity a;
    DisplayController displayController;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        a=this;
        themeUtils.onActivityCreateSetTheme(a);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        displayController = new DisplayController();
    }

    public void scanWaypoint(View view) {
        Intent intent = new Intent(NavigationActivity.this, ScanLocationActivity.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            final String msg = data.getStringExtra("returnedData");
            System.out.println(msg);
            //final TextView instructionTextView = addTextViewOverlay(R.id.text_view_id);
            //instructionTextView.bringToFront();
            //final DisplayController displayController = new DisplayController();
            //displayController.removeOverlay(t);
            //t.bringToFront();
            updateCameraOverlay(data.getStringExtra("returnedData"), "red", "white");
            snackbar= Snackbar.make(findViewById(R.id.lay), "", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("CLOSE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Pop up closed", Toast.LENGTH_SHORT).show();
                }
            });
            displayController.addSnackBar(snackbar, "Scanned");
            //seVaAfisa=data.getStringExtra("returnedData");
        }
    }
}