package com.example.myapplication;

import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class DisplayController {

    public void addOverlay(TextView t,  String text, int height, int width) {
        Overlay overlay = new Overlay();
        overlay.draw(t, text, height, width);
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
