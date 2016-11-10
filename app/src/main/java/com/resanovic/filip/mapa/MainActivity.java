package com.resanovic.filip.mapa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.resanovic.filip.mapa.database.WalkDatabaseHelper;

import java.io.File;


/**
 * Created by Filip on 14.9.2016..
 */
public class MainActivity extends AppCompatActivity {
    private Button btnMeasure;
    private Button btnHistory;
    private Button btnDelHistory;
    private Button btnModWeight;
    private Button btnGetWeather;
    private LocationManager locationManager;
    private boolean started = false;
    private int weight;
    private boolean isHistoryDeleted = false;
    private WalkDatabaseHelper helper;
    private SharedPreferences preferences;
    private Intent locationServiceIntent;
    private String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgetsAndObjects();


        if (savedInstanceState != null) {
            btnMeasure.setText(savedInstanceState.getString("button_name"));
        }
        btnMeasure.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                  Toast.makeText(MainActivity.this, R.string.gps_warning, Toast.LENGTH_SHORT).show();
                                                  return;
                                              }
                                              if (started == false) {
                                                  if (!preferences.contains("weight")) {
                                                      inputWeightDialog();
                                                  } else {

                                                      if (isHistoryDeleted == true) {
                                                          isHistoryDeleted = false;
                                                      }
                                                      btnDelHistory.setEnabled(false);
                                                      btnHistory.setEnabled(false);
                                                      started = true;
                                                      locationServiceIntent = new Intent(MainActivity.this, LocationService.class);
                                                      locationServiceIntent.putExtra("weight", weight);
                                                      btnMeasure.setText("Stop");
                                                      startService(locationServiceIntent);
                                                  }
                                              } else {

                                                  stopService(locationServiceIntent);
                                                  btnMeasure.setText("Start");
                                                  started = false;
                                                  btnDelHistory.setEnabled(true);
                                                  btnHistory.setEnabled(true);
                                              }
                                          }
                                      }

        );
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                intent.putExtra("isDeleted", isHistoryDeleted);
                startActivity(intent);
            }
        });
        btnDelHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.delete_entry))
                        .setMessage(getString(R.string.you_sure))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteTable();
                                File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "coordinates.txt");
                                if (file.exists()) {
                                    file.delete();
                                }
                                isHistoryDeleted = true;
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                return;
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        btnModWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modWeightDialog();
            }
        });

        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLocationNameDialog();
            }
        });

    }

    private void callWeatherMan() {
        WeatherMan weatherMan = new WeatherMan(this);
        weatherMan.setLocationName(location);
        weatherMan.getData(MainActivity.this);
    }

    private void inputLocationNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.location_input));
        final EditText input = new EditText(MainActivity.this);
        builder.setMessage(getString(R.string.enter_location));
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.name_location);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                location = input.getText().toString();
                callWeatherMan();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.create().show();
    }


    private void modWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.modify_weight));
        final EditText input = new EditText(MainActivity.this);
        builder.setMessage(getString(R.string.here_modify_weight) + String.valueOf(preferences.getInt("weight", 0)));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                weight = Integer.parseInt(input.getText().toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("weight", weight);
                editor.commit();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.create().show();
    }

    private void inputWeightDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.weight_entry));
        final EditText input = new EditText(MainActivity.this);
        builder.setMessage(getString(R.string.app_requires_weight));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                weight = Integer.parseInt(input.getText().toString());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("weight", weight);
                editor.commit();
                dialog.dismiss();
                btnModWeight.setEnabled(true);
                Toast.makeText(MainActivity.this, R.string.press_start_again, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                weight = 0;
                dialog.dismiss();
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.create().show();
    }

    private void initWidgetsAndObjects() {
        btnMeasure = (Button) findViewById(R.id.btnMeasure);
        btnHistory = (Button) findViewById(R.id.btnHistory);
        btnDelHistory = (Button) findViewById(R.id.btnDelHistory);
        btnModWeight = (Button) findViewById(R.id.btnModWeight);
        btnGetWeather = (Button) findViewById(R.id.btnGetWeather);
        preferences = getSharedPreferences("PreferenceData", Context.MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        helper = new WalkDatabaseHelper(this);
        if (preferences.contains("weight")) {
            weight = preferences.getInt("weight", 0);
            btnModWeight.setEnabled(true);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("button_name", btnMeasure.getText().toString());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationServiceIntent != null) {
            stopService(locationServiceIntent);
        } else {
            locationServiceIntent = new Intent(this, LocationService.class);
            stopService(locationServiceIntent);
        }
    }
}
