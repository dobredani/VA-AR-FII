package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.Schedule.SchedView;
import com.example.myapplication.problem.Location;
import com.example.myapplication.problem.LocationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimetableActivity extends AppCompatActivity {
    Dialog officeDialog;
    ApplicationData appData = new ApplicationData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        getSupportActionBar().hide();
        officeDialog = new Dialog(this);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                populateListView(newText);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showInputMethod(view.findFocus());
                }
            }
        });
        populateListView("");
    }

    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    private void populateListView(String lookupText) {
        final GridView gv = (GridView) findViewById(R.id.gridView);
        List<Location> locations = appData.getCurrentBuilding().getLocations();
        List<String> locationStrings = new ArrayList<>();
        lookupText = lookupText.toLowerCase();
        for (Location temp:locations)
            if ((temp.getLocationType() == LocationType.CLASSROOM ||
                    temp.getLocationType() == LocationType.OFFICE) && temp.getName().toLowerCase().contains(lookupText))
                locationStrings.add(temp.getName());
        for (int i = 1; i <= 50; i++) {
            locationStrings.add("lala");
        }

        List<String> locationsList = new ArrayList<String>(locationStrings);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, locationsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundResource(R.drawable.rounded_corners);

                GradientDrawable drawable = (GradientDrawable) tv.getBackground();
                drawable.setColor(Color.WHITE);

                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                /*final Location location = appData.getCurrentBuilding().getLocation(tv.getText().toString());
                if (location.getLocationType() == LocationType.CLASSROOM) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TimetableActivity.this, SchedView.class);
                            intent.putExtra("room", ((TextView) v).getText());
                            startActivity(intent);
                            System.out.println(((TextView) v).getText());
                        }
                    });
                }
                else if (location.getLocationType() == LocationType.OFFICE) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOfficeData(location);
                        }
                    });
                }*/
                return view;
            }
        };
        gv.setAdapter(arrayAdapter);
    }

    public void showOfficeData(Location location) {
        officeDialog.setContentView(R.layout.office_popup);
        LinearLayout popup = (LinearLayout) officeDialog.findViewById(R.id.errorInfo);
        popup.setVisibility(View.VISIBLE);
        TextView infoTextView = (TextView) officeDialog.findViewById(R.id.officeInfo);
        infoTextView.setText("test");
        Button closePopup = (Button) officeDialog.findViewById(R.id.refreshBtn);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                officeDialog.dismiss();
            }
        });
        officeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        officeDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        officeDialog.show();
    }
}
