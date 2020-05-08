package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NavigationActivity extends CameraActivity {

    DisplayController displayController;
    List<String> instructions;
    List<Integer> codesToScan;
    int currentIndex = 1;
    Snackbar snackbar;
    Dialog helpDialog;
    Button closePopup;
    LinearLayout popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hides the action bar
        getSupportActionBar().hide();
        //hides the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        helpDialog = new Dialog(this);

        setContentView(R.layout.activity_navigation);
        displayController = new DisplayController();
        Intent intent = getIntent();
        instructions = intent.getStringArrayListExtra("instructions");
        
        codesToScan = intent.getIntegerArrayListExtra("codesToScan");

        snackbar = Snackbar.make(findViewById(R.id.lay), "", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Close", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Pop up closed", Toast.LENGTH_SHORT).show();
            }
        });

        currentInstruction = instructions.get(0);
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
                displayController.addSnackBar(snackbar, "Wrong turn, if you are lost press Restart Navigation.");
                codeScanned = -1;
            }
            if (codeScanned != codesToScan.get(currentIndex))
                displayController.addSnackBar(snackbar, "Wrong turn, if you are lost press Restart Navigation.");
            else {
                currentIndex++;
                if (currentIndex == instructions.size()) {
                    displayController.addSnackBar(snackbar, "Congratulations, you have reached your destination!");
                    currentInstruction = instructions.get(currentIndex - 1);
                    Button button = findViewById(R.id.scanWaypointButton);
                    button.setClickable(false);
                } else
                    currentInstruction = instructions.get(currentIndex - 1);

            }
        }
    }

    public void restartNavigation(View view) {
        finish();
    }

    public void showHelp(View view) {
        helpDialog.setContentView(R.layout.help_popup);
        popup = (LinearLayout) helpDialog.findViewById(R.id.helpInfo);
        popup.setVisibility(View.VISIBLE);
        closePopup = (Button) helpDialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        helpDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        helpDialog.show();
    }
}