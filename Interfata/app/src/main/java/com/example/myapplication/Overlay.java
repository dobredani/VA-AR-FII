package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Color;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class Overlay  extends  CameraActivity{

    public void draw(TextView t, String colorText, String colorBackground, String text, int height, int width) {
        t.setTextColor(Color.parseColor(colorText));
        t.setBackgroundColor(Color.parseColor(colorBackground));
        t.setText(text);
        t.setHeight(height);
        t.setWidth(width);
        t.bringToFront();
    }

    public void drawSnackBar(Snackbar snackbar, String text){
       snackbar.setText(text);
       snackbar.show();
    }

    public String setTextColor(String colorText)
    {
        String defaultColor = "gray";
        if(colorText == "")
        return  defaultColor;
        return  colorText;
    }

   public String setTextColorBackground(String colorTextBackground)
    {
        String defaultColor = "white";
        if(colorTextBackground == "")
            return  defaultColor;
        return  colorTextBackground;
    }


}
