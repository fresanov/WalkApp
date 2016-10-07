package com.resanovic.filip.mapa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {
    private TextView tvCityName;
    private TextView tvTemp1;
    private TextView tvTemp2;
    private TextView tvTemp3;
    private TextView tvTemp4;
    private TextView tvTemp5;
    private TextView tvWeather1;
    private TextView tvWeather2;
    private TextView tvWeather3;
    private TextView tvWeather4;
    private TextView tvWeather5;
    private TextView tvTime1;
    private TextView tvTime2;
    private TextView tvTime3;
    private TextView tvTime4;
    private TextView tvTime5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tvCityName = (TextView) findViewById(R.id.tvCityName);
        tvTemp1 = (TextView) findViewById(R.id.tvTemp1);
        tvTemp2 = (TextView) findViewById(R.id.tvTemp2);
        tvTemp3 = (TextView) findViewById(R.id.tvTemp3);
        tvTemp4 = (TextView) findViewById(R.id.tvTemp4);
        tvTemp5 = (TextView) findViewById(R.id.tvTemp5);
        tvWeather1 = (TextView) findViewById(R.id.tvWeather1);
        tvWeather2 = (TextView) findViewById(R.id.tvWeather2);
        tvWeather3 = (TextView) findViewById(R.id.tvWeather3);
        tvWeather4 = (TextView) findViewById(R.id.tvWeather4);
        tvWeather5 = (TextView) findViewById(R.id.tvWeather5);
        tvTime1 = (TextView)findViewById(R.id.tvTime1);
        tvTime2 = (TextView)findViewById(R.id.tvTime2);
        tvTime3 = (TextView)findViewById(R.id.tvTime3);
        tvTime4 = (TextView)findViewById(R.id.tvTime4);
        tvTime5 = (TextView)findViewById(R.id.tvTime5);
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("city_name");
        String[] time = intent.getStringArrayExtra("time");
        String[] weather = intent.getStringArrayExtra("weather");
        String[] temp = intent.getStringArrayExtra("temp");
        if(!cityName.equals("noName")) {
            tvCityName.setText(cityName);
            tvTemp1.setText(temp[0]);
            tvTemp2.setText(temp[1]);
            tvTemp3.setText(temp[2]);
            tvTemp4.setText(temp[3]);
            tvTemp5.setText(temp[4]);
            tvWeather1.setText(weather[0]);
            tvWeather2.setText(weather[1]);
            tvWeather3.setText(weather[2]);
            tvWeather4.setText(weather[3]);
            tvWeather5.setText(weather[4]);
            tvTime1.setText(time[0]);
            tvTime2.setText(time[1]);
            tvTime3.setText(time[2]);
            tvTime4.setText(time[3]);
            tvTime5.setText(time[4]);
        } else {
            Toast.makeText(WeatherActivity.this, "Location name not found", Toast.LENGTH_SHORT).show();
        }

    }
}
