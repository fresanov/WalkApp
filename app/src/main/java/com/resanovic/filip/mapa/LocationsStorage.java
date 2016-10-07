package com.resanovic.filip.mapa;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Filip on 24.9.2016..
 */
public class LocationsStorage {

    private Context mContext;
    private List<LatLng> locationsList;
    private double distance = 0.0;
    private File file;
    private UUID id;


    public LocationsStorage(Context context) {
        mContext = context;
        file = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "coordinates.txt");
    }

    public void writeData(double lat, double lon, boolean first) {
        /*try {
            OutputStreamWriter writer = new OutputStreamWriter(mContext.openFileOutput("locations.txt", Context.MODE_PRIVATE));
            writer.write(String.valueOf(lat) + "\t" + String.valueOf(lon) + "\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            FileWriter writer = new FileWriter(file, true);
            if (first == true) {
                id = UUID.randomUUID();
                writer.append(id.toString() + "\n");
            }
            writer.append(String.valueOf(lat) + "\t" + String.valueOf(lon) + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public double getDistance() {
        try {
            InputStream inputStream = new FileInputStream(file);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                locationsList = new ArrayList<>();
                while (!line.equals(id.toString())) {
                    line = bufferedReader.readLine();
                }
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.indexOf("\t") == -1) break;
                    double lat = Double.parseDouble(line.substring(0, line.indexOf("\t")));
                    double lon = Double.parseDouble(line.substring(line.indexOf("\t") + 1, line.length()));
                    locationsList.add(new LatLng(lat, lon));
                    if (locationsList.size() > 1000) {
                        distance += SphericalUtil.computeLength(locationsList);
                        locationsList.clear();
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        distance += SphericalUtil.computeLength(locationsList);
        return distance;
    }

    public PolylineOptions getSpecificRoute(String uuid){
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE);
        try {
            InputStream inputStream = new FileInputStream(file);
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while (!line.equals(uuid.toString())) {
                    line = bufferedReader.readLine();
                }
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.indexOf("\t") == -1) break;
                    double lat = Double.parseDouble(line.substring(0, line.indexOf("\t")));
                    double lon = Double.parseDouble(line.substring(line.indexOf("\t") + 1, line.length()));
                    options.add(new LatLng(lat, lon));
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return options;
    }

    public String getId() {

        return id.toString();
    }


}
