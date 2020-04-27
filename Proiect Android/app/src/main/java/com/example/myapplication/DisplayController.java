package com.example.myapplication;

import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class DisplayController {

    public void addOverlay(TextView t, String colorText, String colorBackground, String text, int height, int width) {
        Overlay overlay = new Overlay();
        String textColor = overlay.setTextColor(colorText);
        String backgroundColor = overlay.setTextColorBackground(colorBackground);
        overlay.draw(t, textColor, backgroundColor, text, height, width);
    }

    public void addSnackBar(Snackbar snackbar, String text) {
        Overlay overlay = new Overlay();
        overlay.drawSnackBar(snackbar, text);
    }

    public void removeOverlay(TextView t) {
        t.setText("");

    }

    public void refresh() {

    }


}
