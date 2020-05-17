package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;

public class NavigationActivity extends CameraActivity {

    Activity a;
    DisplayController displayController;
    List<String> instructions;
    List<Integer> codesToScan;
    int currentIndex = 1;
    Snackbar snackbar;
    Dialog helpDialog, endOfNavigationDialog;
    Button closePopup;
    LinearLayout popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        a = this;
        themeUtils.onActivityCreateSetTheme(a);
        //hides the action bar
        Objects.requireNonNull(getSupportActionBar()).hide();
        //hides the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        helpDialog = new Dialog(this);
        endOfNavigationDialog = new Dialog(this);

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
            int codeScanned = -1;
            try {
                if (msg != null)
                    codeScanned = Integer.parseInt(msg);
            } catch (NumberFormatException e) {
                displayController.addSnackBar(snackbar, "Wrong turn, if you are lost press the X button in the upper right corner.");
                codeScanned = -1;
            }
            if (codeScanned != codesToScan.get(currentIndex))
                displayController.addSnackBar(snackbar, "Wrong turn, if you are lost press the X button in the upper right corner.");
            else {
                currentIndex++;
                if (currentIndex == instructions.size()) {
                    showEndNavigationMessage();
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

    public void showEndNavigationMessage(){
        helpDialog.setContentView(R.layout.end_navigation_popup);
        popup = helpDialog.findViewById(R.id.endNavInfo);
        popup.setVisibility(View.VISIBLE);
        closePopup = helpDialog.findViewById(R.id.goBackBtn);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
                finish();
            }
        });
        Objects.requireNonNull(helpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        helpDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        helpDialog.show();

    }
    public void showHelp(View view) {
        helpDialog.setContentView(R.layout.help_popup);
        popup = helpDialog.findViewById(R.id.helpInfo);
        popup.setVisibility(View.VISIBLE);
        closePopup = helpDialog.findViewById(R.id.closePopup);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        Objects.requireNonNull(helpDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        helpDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        helpDialog.show();
    }
    public void getNextInstruction(View view) {
        if (currentIndex < instructions.size()) {
            currentIndex++;
            currentInstruction = instructions.get(currentIndex - 1);
            final TextView helloTextView = addTextViewOverlay(R.id.text_view_id);
            displayController.addOverlay(helloTextView, currentInstruction);
        }
    }

    public void getPreviousInstruction(View view) {
        if (currentIndex > 1 && currentIndex <= instructions.size() + 1) {
            currentIndex--;
            currentInstruction = instructions.get(currentIndex - 1);
            final TextView helloTextView = addTextViewOverlay(R.id.text_view_id);
            displayController.addOverlay(helloTextView, currentInstruction);
        }
    }
}