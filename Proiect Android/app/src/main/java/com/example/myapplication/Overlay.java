package com.example.myapplication;

import android.graphics.Color;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Overlay {

    public void draw(TextView t, String text, int height, int width) {
        t.setText(text);
        t.setHeight(height);
        t.setWidth(width);
        t.bringToFront();
    }

    public void drawSnackBar(Snackbar snackbar, String text) {
        snackbar.setText(text);
        snackbar.show();
    }


}
