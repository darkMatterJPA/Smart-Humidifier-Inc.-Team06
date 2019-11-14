package com.example.humidifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    Humidifier humidifier;
    boolean mServiceBound = false;

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Humidifier.LocalBinder binder = (Humidifier.LocalBinder) service;
            humidifier = binder.getService();
            mServiceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mServiceBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, Humidifier.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mServiceBound  = false;
    }


    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        graph = (GraphView) findViewById(R.id.graph);

       // https://github.com/jjoe64/GraphView/wiki/Bar-Graph
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);





            //https://weeklycoding.com/mpandroidchart-documentation/
        //https://weeklycoding.com/mpandroidchart-documentation/setting-data/
        //https://javadoc.jitpack.io/com/github/PhilJay/MPAndroidChart/v3.1.0/javadoc/


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_home:
                        Toast.makeText(ChartActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(ChartActivity.this, MainActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_graph:
                        Toast.makeText(ChartActivity.this, "Graph", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_schedule:
                        Toast.makeText(ChartActivity.this, "Schedule", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(ChartActivity.this, UserScheduleActivity.class);
                        startActivity(a);
                        break;

                }
                return true;
            }
        });
    }


}
