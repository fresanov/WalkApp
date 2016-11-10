package com.resanovic.filip.mapa;


import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Filip on 28.9.2016..
 */
public class WeatherMan extends AppCompatActivity {
    private static final String API_KEY = "3fffb6e2c5054fc59d4c17bafeb0b482";

    private String jsonData;
    private String city_name;
    private String locationName;
    private String[] temp = new String[5];
    private String[] weather = new String[5];
    private String[] time = new String[5];
    private Context mContext;
    private JSONObject arrayBodyJsonObject;
    private JSONArray weatherJsonArray;
    private JSONObject weatherObject;
    private JSONObject mainJsonObject;
    private Context context;

    public WeatherMan (Context context){
        this.context = context;
    }


    public byte[] getUrlBytes(String UrlSpec) throws IOException {
        URL url = new URL(UrlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + UrlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void fetchWeatherData() {
        try {
            String url = Uri.parse("http://api.openweathermap.org/data/2.5/forecast/city?").buildUpon()
                    .appendQueryParameter("q", locationName + ",hr")
                    .appendQueryParameter("APPID", API_KEY)
                    .build().toString();
            jsonData = getUrlString(url);
        } catch (IOException e) {
            Log.d("exception", e.getMessage());
        }
    }

    private void parseData(String jsonData) throws IOException, JSONException {
        JSONObject jsonBody = new JSONObject(jsonData);
        try {
            JSONObject cityJsonObject = jsonBody.getJSONObject("city");
            city_name = cityJsonObject.getString("name");
            JSONArray listJsonArray = jsonBody.getJSONArray("list");
            for (int i = 0; i < 5; i++) {
                arrayBodyJsonObject = listJsonArray.getJSONObject(i);
                time[i] = arrayBodyJsonObject.getString("dt_txt").substring(0, arrayBodyJsonObject.getString("dt_txt").length() - 3);
                weatherJsonArray = arrayBodyJsonObject.getJSONArray("weather");
                weatherObject = weatherJsonArray.getJSONObject(0);
                weather[i] = weatherObject.getString("main");
                mainJsonObject = arrayBodyJsonObject.getJSONObject("main");
                double temperature = Double.parseDouble(mainJsonObject.getString("temp"));
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                double temperatureCel = Double.valueOf(df.format(temperature - 273));
                temp[i] = String.valueOf(temperatureCel);
            }
        } catch (JSONException je) {
            city_name = "noName";
        }

    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    private class WeatherManTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog = new ProgressDialog(context);
        @Override
        protected Void doInBackground(Void... params) {

            fetchWeatherData();
            try {
                parseData(jsonData);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Fetching data...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
            Intent i = new Intent(mContext, WeatherActivity.class);
            i.putExtra("city_name", city_name);
            i.putExtra("temp", temp);
            i.putExtra("time", time);
            i.putExtra("weather", weather);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);

        }


    }

    public void getData(Context context) {
        mContext = context.getApplicationContext();
        new WeatherManTask().execute();
    }

}
