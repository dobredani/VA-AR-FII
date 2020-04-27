package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NavigationActivity extends CameraActivity {

    DisplayController displayController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            final TextView instructionTextView = addTextViewOverlay(R.id.text_view_id);
            final DisplayController displayController = new DisplayController();
            displayController.removeOverlay(instructionTextView);
            displayController.addOverlay(instructionTextView, "black", "white", msg, 90, 135);
        }
    }
}