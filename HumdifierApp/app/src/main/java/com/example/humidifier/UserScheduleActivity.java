package com.example.humidifier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class UserScheduleActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter aAdapter;
    private String[] ScheduleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);
        mListView = (ListView) findViewById(R.id.userlist);
        aAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ScheduleData);
        mListView.setAdapter(aAdapter);


    }
}
