package com.example.myapplication;

import android.graphics.Color;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Overlay {

    public void draw(TextView t, String colorText, String colorBackground, String text, int height, int width) {
        t.setText(text);
        t.setTextColor(Color.parseColor(colorText));
        t.setBackgroundColor(Color.parseColor(colorBackground));
        t.setHeight(height);
        t.setWidth(width);
        t.bringToFront();
    }

    public void drawSnackBar(Snackbar snackbar, String text) {
        snackbar.setText(text);
        snackbar.show();
    }

    public String setTextColor(String colorText) {
        String defaultColor = "gray";
        if (colorText == "")
            return defaultColor;
        return colorText;
    }

    public String setTextColorBackground(String colorTextBackground) {
        String defaultColor = "white";
        if (colorTextBackground == "")
            return defaultColor;
        return colorTextBackground;
    }


}
