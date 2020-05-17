package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
public class themeUtils {

    static int cTheme;
    final static int  Light_Theme= 0;
    final static int Dark_Theme=1;

    static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.startActivity(activity.getIntent());
        activity.finish();
    }

    public static void onActivityCreateSetTheme(Activity activity){
        switch(cTheme){
            default:
            case Dark_Theme:
                activity.setTheme(R.style.DarkTheme);
                break;
            case Light_Theme:
                activity.setTheme(R.style.AppTheme);
                break;
        }
    }
}
