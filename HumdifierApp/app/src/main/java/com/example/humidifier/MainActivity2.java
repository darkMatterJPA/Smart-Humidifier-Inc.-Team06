package com.example.humidifier;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


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


    // initiate a Spinner
    Spinner spin;
    private Button cancelButton;
    private Button saveButton;


    //Day buttons
    ToggleButton tSu;
    ToggleButton tM;
    ToggleButton tT;
    ToggleButton tW;
    ToggleButton tTh;
    ToggleButton tF;
    ToggleButton tS;

    TimePicker endTimePicker;
    TimePicker startTimePicker;

    String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Time picker
        startTimePicker = (TimePicker) this.findViewById(R.id.startTimePicker);
        startTimePicker.setIs24HourView(false);

        //Time picker
        endTimePicker = (TimePicker) this.findViewById(R.id.endTimePicker);
        endTimePicker.setIs24HourView(false);

        cancelButton = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.save);
        //days of week
        tSu = findViewById(R.id.tSu);
        tM = findViewById(R.id.tM);
        tT = findViewById(R.id.tT);
        tW = findViewById(R.id.tW);
        tTh = findViewById(R.id.tTh);
        tF = findViewById(R.id.tF);
        tS = findViewById(R.id.tS);

        cancelButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v){
            Intent schedActivityIntent = new Intent(getApplicationContext(), UserScheduleActivity.class);
            startActivity(schedActivityIntent);
        }});

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                if(mServiceBound) {
                    String start = (Integer.toString(startTimePicker.getHour())+Integer.toString(startTimePicker.getMinute()));
                    String end = (Integer.toString(endTimePicker.getHour())+Integer.toString(endTimePicker.getMinute()));



                    //Check individual items.
                    if(tSu.isChecked()){
                        humidifier.schedule.Sunday.add(new Schedule.startStop(start,end,level));
                    }
                    if( tM.isChecked()){
                        humidifier.schedule.Monday.add(new Schedule.startStop(start,end,level));
                    }
                    if(tT.isChecked()){
                        humidifier.schedule.Tuesday.add(new Schedule.startStop(start,end,level));
                    }
                    if(tW.isChecked()){
                        humidifier.schedule.Wednesday.add(new Schedule.startStop(start,end,level));
                    }
                    if(tTh.isChecked()){
                        humidifier.schedule.Thursday.add(new Schedule.startStop(start,end,level));
                    }
                    if(tF.isChecked()){
                        humidifier.schedule.Friday.add(new Schedule.startStop(start,end,level));
                    }
                    if(tS.isChecked()){
                        humidifier.schedule.Saturday.add(new Schedule.startStop(start,end,level));
                    }

                    Intent schedActivityIntent = new Intent(getApplicationContext(), UserScheduleActivity.class);
                    startActivity(schedActivityIntent);
                }
            }});

        //Spinner for humidity level
        spin = findViewById(R.id.spinner2);
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


    }
    //home button on bottom nav bar
    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    //used for humidity level spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
      level = parent.getItemAtPosition(position).toString();


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
