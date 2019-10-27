package com.example.humidifier;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity2 extends AppCompatActivity {


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


        TimePicker tp = (TimePicker) this.findViewById(R.id.time_picker);
        tp.setIs24HourView(true);

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
                        break;

                }
                return true;
            }
        });


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
            markedButtons +="S";
        }
      //  Toast.makeText(this, markedButtons, Toast.LENGTH_SHORT).show();

    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }



}
