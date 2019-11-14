package com.example.humidifier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserScheduleActivity extends AppCompatActivity {

    Humidifier humidifier;
    boolean mServiceBound = false;
    private Button deleteAll;
    EditText schedule;

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

        schedule.setText(humidifier.schedule.toString());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mServiceBound  = false;
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        deleteAll = this.findViewById(R.id.delete);

        deleteAll.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                humidifier.schedule.deleteAll();
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_schedule:
                        Toast.makeText(UserScheduleActivity.this, "Schedule", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_home:
                        Toast.makeText(UserScheduleActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(UserScheduleActivity.this, MainActivity.class);
                        startActivity(a);
                        break;
                    case R.id.action_graph:
                        Toast.makeText(UserScheduleActivity.this, "Graph", Toast.LENGTH_SHORT).show();
                        Intent g = new Intent(UserScheduleActivity.this, ChartActivity.class);
                        startActivity(g);
                        break;

                }
                return true;
            }
        });

    }
}
