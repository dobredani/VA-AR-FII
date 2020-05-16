package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
public class themeUtils {
    public static int cTheme;
    public final static int  Light_Theme= 0;
    public final static int Dark_Theme=1;
    public static void changeToTheme(Activity activity, int theme) {
        cTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
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
