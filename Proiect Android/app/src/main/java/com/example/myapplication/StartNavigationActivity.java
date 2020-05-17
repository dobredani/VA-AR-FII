package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.problem.Location;
import com.example.myapplication.problem.LocationType;
import com.example.myapplication.problem.Waypoint;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.widget.Toast.*;

public class StartNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActionBarDrawerToggle mToggle;
    private ActionBar actionBar;
    private Location start, destination;
    private Dialog errorDialog;
    public Activity a;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a = this;
        themeUtils.onActivityCreateSetTheme(this);

        errorDialog = new Dialog(this);

        setContentView(R.layout.start_navigation);
        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setTitle(ApplicationData.getInstance().getCurrentBuilding().getName());
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ImageView scanLocation = findViewById(R.id.cameraBtn);
        scanLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCameraPermission("cameraBtn");

            }
        });
        startNavigationButton();
        generateSuggestedPlaces(4);
    }

    private void startNavigationButton() {
        final Button startNavigation = findViewById(R.id.navigationBtn);
        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavigation.setEnabled(false);
                String startName = ((TextView) findViewById(R.id.currentLocation)).getText().toString();
                Location start = ApplicationData.getInstance().getCurrentBuilding().getLocation(startName);
                if (start == null || start.getLocationType() == LocationType.CONNECTOR) {
                    makeText(getApplicationContext(), "Invalid Starting Point", LENGTH_SHORT).show();
                    startNavigation.setEnabled(true);

                } else {
                    String destinationName = ((TextView) findViewById(R.id.destination)).getText().toString();
                    Location destination = ApplicationData.getInstance().getCurrentBuilding().getLocation(destinationName);
                    if (destination == null || destination.getLocationType() == LocationType.CONNECTOR) {
                        makeText(getApplicationContext(), "Invalid Destination", LENGTH_SHORT).show();
                        startNavigation.setEnabled(true);
                    } else {
                        requestCameraPermission("navigationBtn");
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.chooseBuilding:
                showBuildings();
                return true;
            case R.id.theme:
                showThemes();
                return true;
            case R.id.help:
                startSpecificActivity();
                return true;
            default: return false;
        }
    }

    public void startSpecificActivity() {
        Intent intent = new Intent(StartNavigationActivity.this, HelpActivity.class);
        startActivity(intent);
    }

    private void showThemes(){
        final String[] themes ={"Purple Theme", "Blue Theme"};

        AlertDialog.Builder bld=new AlertDialog.Builder(this);
        bld.setTitle("Choose a theme");
        bld.setItems(themes, new DialogInterface.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences mPrefs;
                SharedPreferences.Editor mEditor;
                switch(which){
                    case 1:
                        themeUtils.changeToTheme(MainActivity.a, themeUtils.Dark_Theme);
                        themeUtils.changeToTheme(a, themeUtils.Dark_Theme);
                        mPrefs = getSharedPreferences("THEME", 0);
                        mEditor = mPrefs.edit();
                        mEditor.putInt("theme", 1).commit();
                        break;
                    case 0:
                        themeUtils.changeToTheme(MainActivity.a, themeUtils.Light_Theme);
                        themeUtils.changeToTheme(a, themeUtils.Light_Theme);
                        mPrefs = getSharedPreferences("THEME", 0);
                        mEditor = mPrefs.edit();
                        mEditor.putInt("theme", 0).commit();
                        break;
                    default:
                        System.out.println(which);
                }
            }
        });
        bld.show();
    }

    private void showBuildings() {
        List<String> buildingNames = ApplicationData.getInstance().getBuildings();
        final String[] buildings = new String[buildingNames.size()];
        int i = 0;
        for (String temp : buildingNames) {
            buildings[i] = temp;
            i++;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a building");
        builder.setItems(buildings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               //Z ApplicationData.getInstance().setCurrentBuildingName(buildings[which]);
                ApplicationData.getInstance().setCurrentBuilding(ApplicationData.getInstance().getBuildingByName(buildings[which]));
                generateSuggestedPlaces(4);
                actionBar = getSupportActionBar();
                Objects.requireNonNull(actionBar).setTitle(buildings[which]);
            }

        });
        builder.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String msg = data.getStringExtra("returnedData");
            Location location;
            if (msg != null) {
                location = ApplicationData.getInstance().getCurrentBuilding().getLocationById(Integer.parseInt(msg));
                EditText editText = findViewById(R.id.currentLocation);
                editText.setText(location.getName());
            }
        }
    }

    public void getWaypoints(String start, String destination) {
        // the url is different for every computer.
        // for emulator use 10.0.0.2:5000/
        // for device, run ipconfig in cmd and get ipv4 address

        String url = "http://192.168.100.3:5000/route/";

        url = url.concat(ApplicationData.getInstance().getCurrentBuilding().getName() + "?start=" + start + "&" + "destination=" + destination);
        System.out.println(url);
        final RequestQueue requestQueue = Volley.newRequestQueue(StartNavigationActivity.this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JsonObject", "Response: " + response.toString());

                        List<Waypoint> waypointList = JsonParser.parseRoute(response);
                        for (Waypoint waypoint : waypointList)
                            System.out.println(waypoint);
                        ApplicationData.getInstance().setWaypoints(waypointList);
                        Intent intent = new Intent(StartNavigationActivity.this, NavigationActivity.class);
                        intent.putStringArrayListExtra("instructions", ApplicationData.getInstance().getAllInstructions());
                        intent.putIntegerArrayListExtra("codesToScan", ApplicationData.getInstance().getAllCodesToScan());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showPopup();
                        Log.e("JsonError", "Error on get JSON request!");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void generateSuggestedPlaces(int howMany) {
        final ListView lv = findViewById(R.id.listView);
        List<Location> topLocations = ApplicationData.getInstance().getCurrentBuilding().getTopLocations(howMany);

        String[] locations = new String[topLocations.size()];

        int i = 0;
        for (Location temp : topLocations) {
            locations[i] = temp.getName();
            i++;
        }

        List<String> locationsList = new ArrayList<String>(Arrays.asList(locations));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.custom_list_item, locationsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                //TextView tv = view.findViewById(R.id.list_item_text);
                return super.getView(position, convertView, parent);
            }
        };
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                TextView startTextView = findViewById(R.id.currentLocation);
                TextView destinationTextView = findViewById(R.id.destination);
                String myLocation = (String) parent.getAdapter().getItem(position);
                String startName = startTextView.getText().toString();
                String destinationName = destinationTextView.getText().toString();
                if (startName.length()==0) {
                    //start = ApplicationData.getInstance().currentBuilding.getLocation(myLocation);
                    startTextView.setText(myLocation);
                } else if (destinationName.length()==0) {
                    //destination = ApplicationData.getInstance().currentBuilding.getLocation(myLocation);
                    destinationTextView.setText(myLocation);
                } else {
                    makeText(getApplicationContext(), "You already have an input", LENGTH_SHORT).show();
                }

            }

        });
    }

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    public void showPopup() {
        errorDialog.setContentView(R.layout.error_popup);
        LinearLayout popup = errorDialog.findViewById(R.id.errorInfo);
        popup.setVisibility(View.VISIBLE);
        Button closePopup = errorDialog.findViewById(R.id.refreshBtn);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                refreshActivity();
            }
        });
        Objects.requireNonNull(errorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errorDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        errorDialog.show();
    }

    public void openTimetableActivity(MenuItem item) {
        Intent intent = new Intent(StartNavigationActivity.this, TimetableActivity.class);
        startActivity(intent);
    }

    public void requestCameraPermission(final String check) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted
                        if(check.equals("cameraBtn")){
                            Intent intent = new Intent(StartNavigationActivity.this, ScanLocationActivity.class);
                            startActivityForResult(intent, 1);}
                        else{
                            final Button startNavigation = findViewById(R.id.navigationBtn);
                            String startName = ((TextView) findViewById(R.id.currentLocation)).getText().toString();
                            String destinationName = ((TextView) findViewById(R.id.destination)).getText().toString();
                            start = ApplicationData.getInstance().currentBuilding.getLocation(startName);
                            destination = ApplicationData.getInstance().currentBuilding.getLocation(destinationName);
                            getWaypoints(String.valueOf(start.getId()), String.valueOf(destination.getId()));
                            Timer buttonTimer = new Timer();
                            buttonTimer.schedule(new TimerTask() {

                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            startNavigation.setEnabled(true);
                                        }
                                    });
                                }
                            }, 5000);
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            if (check.equals("navigationBtn")) {
                                final Button startNavigation = findViewById(R.id.navigationBtn);
                                startNavigation.setEnabled(true);
                            }
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        if(check.equals("navigationBtn")){
                            final Button startNavigation = findViewById(R.id.navigationBtn);
                            startNavigation.setEnabled(true);}
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartNavigationActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}