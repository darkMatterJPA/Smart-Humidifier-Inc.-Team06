package com.example.humidifier;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    private Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //back = findViewById(R.id.backButton);
        //back.setOnClickListener(new View.OnClickListener() {
           // @Override
         //   public void onClick(View view) {
             //   finish();
            //}
        //});

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

    }


    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
