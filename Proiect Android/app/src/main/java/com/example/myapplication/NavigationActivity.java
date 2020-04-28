package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NavigationActivity extends CameraActivity {

    DisplayController displayController;
    List<String> instructions;
    List<Integer> codesToScan;
    int currentIndex = 0;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        displayController = new DisplayController();
        Intent intent = getIntent();
        instructions = intent.getStringArrayListExtra("instructions");
        codesToScan = intent.getIntegerArrayListExtra("codesToScan");

        snackbar = Snackbar.make(findViewById(R.id.lay), "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Restart", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Pop up closed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void scanWaypoint(View view) {
        Intent intent = new Intent(NavigationActivity.this, ScanLocationActivity.class);
        startActivityForResult(intent, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            final String msg = data.getStringExtra("returnedData");
            int codeScanned;
            try {
                codeScanned = Integer.parseInt(msg);
            } catch (NumberFormatException e) {
                displayController.addSnackBar(snackbar, "Wrong turn, do you want to restart navigation?");
                codeScanned = -1;
            }
            if (codeScanned != codesToScan.get(currentIndex))
                displayController.addSnackBar(snackbar, "Wrong turn, do you want to restart navigation?");
            else {
                currentIndex++;
                if (currentIndex == instructions.size()) {
                    displayController.addSnackBar(snackbar, "Congratulations, you have reached your destination!");
                    Button button = findViewById(R.id.scanWaypointButton);
                    button.setActivated(false);
                }
                else {
                    System.out.println(instructions.get(currentIndex));
                    final TextView instructionTextView = addTextViewOverlay(R.id.text_view_id);
                    final DisplayController displayController = new DisplayController();
                    displayController.removeOverlay(instructionTextView);
                    displayController.addOverlay(instructionTextView, "black", "white", instructions.get(currentIndex), 90, 135);
                }
            }
        }
    }
}