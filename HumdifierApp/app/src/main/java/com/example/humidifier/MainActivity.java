package com.example.humidifier;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    // initiate a Switch
    Switch simpleSwitch;
    // initiate a Spinner
    Spinner spin;

    String spinValue;

    Humidifier humidifier;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://polar-meadow-51053.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_schedule:
                        Toast.makeText(MainActivity.this, "Schedule", Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(a);
                        break;
                    case R.id.action_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_graph:
                        Toast.makeText(MainActivity.this, "Graph", Toast.LENGTH_SHORT).show();
                        Intent g = new Intent(MainActivity.this, ChartActivity.class);
                        startActivity(g);
                        break;

                }
                return true;
            }
        });


        simpleSwitch = findViewById(R.id.onswitch);
        simpleSwitch.setTextOn("On"); // displayed text of the Switch whenever it is in checked or on state
        simpleSwitch.setTextOff("Off"); // displayed text of the Switch whenever it is in unchecked i.e. o
        //set the current state of a Switch
        simpleSwitch.setChecked(false);


        spin = findViewById(R.id.spinner1);
        spin.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Low");
        categories.add("Medium");
        categories.add("High");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spin.setAdapter(dataAdapter);


        // Button bt1 = findViewById(R.id.schedButton);
        //bt1.setOnClickListener(new View.OnClickListener() {

        //   @Override

        // public void onClick(View view) {
        //   startSchedule();
        //}
        //});

        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleSwitch.getText();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        spinValue = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void startSchedule() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, Humidifier.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
       //  = false;
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Humidifier binder = (Humidifier) service;
            humidifier = binder.getService();
            //mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            //mBound = false;
        }
    };

}





