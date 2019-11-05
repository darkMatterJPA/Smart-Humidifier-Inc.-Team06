package com.example.humidifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // initiate a Spinner
    Spinner spin;
    private Button viewButton;


    //Day buttons
    ToggleButton tSu;
    ToggleButton tM;
    ToggleButton tT;
    ToggleButton tW;
    ToggleButton tTh;
    ToggleButton tF;
    ToggleButton tS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Time picker
        TimePicker tp = (TimePicker) this.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);

        viewButton = findViewById(R.id.viewsched);
        viewButton.setOnClickListener(new View.OnClickListener() {

        public void onClick(View v){
            Intent schedActivityIntent = new Intent(getApplicationContext(), UserScheduleActivity.class);
            startActivity(schedActivityIntent);
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
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_home:
                        Toast.makeText(MainActivity2.this, "Home", Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(MainActivity2.this, MainActivity.class);
                        startActivity(b);
                        break;
                    case R.id.action_graph:
                        Toast.makeText(MainActivity2.this, "Graph", Toast.LENGTH_SHORT).show();
                        Intent g = new Intent(MainActivity2.this, ChartActivity.class);
                        startActivity(g);
                        break;

                }
                return true;
            }
        });

        //days of week
        tSu = findViewById(R.id.tSu);
        tM = findViewById(R.id.tM);
        tT = findViewById(R.id.tT);
        tW = findViewById(R.id.tW);
        tTh = findViewById(R.id.tTh);
        tF = findViewById(R.id.tF);
        tS = findViewById(R.id.tS);

        String markedButtons= "Day Selected";
        //Check individual items.
        if(tSu.isChecked()){
            markedButtons +="Su,";
        }
        if(tM.isChecked()){
            markedButtons +="M,";
        }
        if(tT.isChecked()){
            markedButtons +="T,";
        }
        if(tW.isChecked()){
            markedButtons +="W,";
        }
        if(tTh.isChecked()){
            markedButtons +="Th,";
        }
        if(tF.isChecked()){
            markedButtons +="F,";
        }
        if(tS.isChecked()){
            markedButtons +="S,";
        }
      //  Toast.makeText(this, markedButtons, Toast.LENGTH_SHORT).show();

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
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
