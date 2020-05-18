package com.example.myapplication.util;

import android.graphics.Color;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Overlay {

    public void draw(TextView t, String text) {
        t.setText(text);
        t.bringToFront();
    }

    public void drawSnackBar(Snackbar snackbar, String text) {
        snackbar.setText(text);
        snackbar.show();
    }


}
