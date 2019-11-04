package com.example.humidifier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class UserScheduleActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter aAdapter;
   private String[] ScheduleData ={" "};
    private Button backToSched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        mListView = (ListView) findViewById(R.id.userlist);
       aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ScheduleData);
       mListView.setAdapter(aAdapter);

        backToSched = findViewById(R.id.backButton);
        backToSched.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v){
            Intent Main2ActivityIntent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(Main2ActivityIntent);
        }
    });



    }
}
