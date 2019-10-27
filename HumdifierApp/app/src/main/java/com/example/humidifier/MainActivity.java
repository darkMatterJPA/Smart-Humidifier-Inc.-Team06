package com.example.humidifier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {




    // initiate a Switch
    Switch simpleSwitch;
    // initiate a Spinner
    Spinner spin;

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
                        break;

                }
                return true;
            }
        });


        simpleSwitch =
                findViewById(R.id.onswitch);
        simpleSwitch.setTextOn("On"); // displayed text of the Switch whenever it is in checked or on state
        simpleSwitch.setTextOff("Off"); // displayed text of the Switch whenever it is in unchecked i.e. o
        //set the current state of a Switch
        simpleSwitch.setChecked(false);

        spin =

                findViewById(R.id.spinner1);
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

        netCom com = new netCom();
        com.execute();

    }

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

    private void startSchedule() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }

    private class netCom extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                //                                    Toast.makeText(getApplicationContext(), "Response is: " +
//                                            obj.toString().substring(0, 500), Toast.LENGTH_LONG).show();

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://infinite-journey-50054.herokuapp.com/";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(getApplicationContext(), "Response is: " +
                                        response.toString(), Toast.LENGTH_LONG).show();
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}





