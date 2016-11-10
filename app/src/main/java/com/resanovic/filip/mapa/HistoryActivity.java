package com.resanovic.filip.mapa;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.resanovic.filip.mapa.database.WalkDatabaseHelper;

/**
 * Created by Filip on 16.9.2016..
 */
public class HistoryActivity extends AppCompatActivity {
    private TableLayout tlHistory;
    private WalkDatabaseHelper helper;
    private boolean isHistoryDeleted;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tlHistory = (TableLayout)findViewById(R.id.tlHistory);
        helper = new WalkDatabaseHelper(this);
        Intent intent = getIntent();
        isHistoryDeleted = intent.getBooleanExtra("isDeleted", false);
        if(isHistoryDeleted == false) {
            buildTable();
            helper.close();
        } else {
            tlHistory.removeAllViews();
            setContentView(R.layout.activity_history);
        }
    }

    private void buildTable() {
        Cursor c = helper.getAllData();
        int rows = c.getCount();
        int columns = c.getColumnCount();
        c.moveToFirst();
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            for (int j = 1; j < columns-5; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);

                tv.setText(c.getString(j));

                row.addView(tv);

            }
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor c = helper.getSingleRow(v.getId());

                    if(c.getCount()>=1){
                        c.moveToFirst();
                        Intent intent = new Intent(HistoryActivity.this, MapsActivity.class);
                        intent.putExtra("distance", c.getDouble(2));
                        intent.putExtra("steps", c.getInt(3));
                        intent.putExtra("calories", c.getInt(4));
                        intent.putExtra("lat_start", c.getDouble(5));
                        intent.putExtra("lon_start", c.getDouble(6));
                        intent.putExtra("lat_stop", c.getDouble(7));
                        intent.putExtra("lon_stop", c.getDouble(8));
                        intent.putExtra("uuid",c.getString(9));
                        startActivity(intent);
                    } else {
                        Toast.makeText(HistoryActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            });
            row.setId(c.getInt(0));
            c.moveToNext();
            row.setBackgroundResource(R.drawable.row_border);
            tlHistory.addView(row);

        }
        c.close();
    }
}
