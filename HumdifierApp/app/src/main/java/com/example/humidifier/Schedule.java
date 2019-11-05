package com.example.humidifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Schedule {

    public class startStop{
        int start;
        int stop;

        public startStop()
        {
            start = 0;
            stop = 0;
        }

    }

    ArrayList<startStop> Monday;
    ArrayList<startStop> Tuesday;
    ArrayList<startStop> Wednesday;
    ArrayList<startStop> Thursday;
    ArrayList<startStop> Friday;
    ArrayList<startStop> Saturday;
    ArrayList<startStop> Sunday;

    JSONObject obj = new JSONObject();

    public Schedule (){
        Monday = new ArrayList<>();
        Tuesday = new ArrayList<>();
        Wednesday = new ArrayList<>();
        Thursday = new ArrayList<>();
        Friday = new ArrayList<>();
        Saturday = new ArrayList<>();
        Sunday = new ArrayList<>();
    }

}
