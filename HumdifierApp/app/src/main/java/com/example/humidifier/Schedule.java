package com.example.humidifier;


import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Schedule {

    public static class startStop{
        String start;
        String stop;
        int humidityLevel;

        public startStop(String start, String stop, int humidityLevel )
        {
            this.start = start;
            this.stop = stop;
            this.humidityLevel = humidityLevel;

        }

        @NonNull
        @Override
        public String toString() {
            return "Start: "+start+", Stop: "+stop+", Humidity Level: "+humidityLevel;
        }

        public JSONObject toJSONObj()
        {
            JSONObject obj = new JSONObject();
            try {
                obj.put("startTime",start);
                obj.put("endTime", stop);
                obj.put("humidityLevel", humidityLevel);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return obj;
        }
    }

    ArrayList<startStop> Monday;
    ArrayList<startStop> Tuesday;
    ArrayList<startStop> Wednesday;
    ArrayList<startStop> Thursday;
    ArrayList<startStop> Friday;
    ArrayList<startStop> Saturday;
    ArrayList<startStop> Sunday;



    public Schedule (){
        Monday = new ArrayList<>();
        Tuesday = new ArrayList<>();
        Wednesday = new ArrayList<>();
        Thursday = new ArrayList<>();
        Friday = new ArrayList<>();
        Saturday = new ArrayList<>();
        Sunday = new ArrayList<>();
    }

    public JSONObject getJsonSchedule (){
        JSONObject obj = new JSONObject();

        JSONArray mArr = new JSONArray();
        for(Schedule.startStop i : Monday){
            mArr.put(i.toJSONObj());
        }

        JSONArray tArr = new JSONArray();
        for(Schedule.startStop i : Tuesday){
            tArr.put(i.toJSONObj());
        }

        JSONArray wArr = new JSONArray();
        for(Schedule.startStop i : Wednesday){
            wArr.put(i.toJSONObj());
        }

        JSONArray thArr = new JSONArray();
        for(Schedule.startStop i : Thursday){
            thArr.put(i.toJSONObj());
        }

        JSONArray fArr = new JSONArray();
        for(Schedule.startStop i : Friday){
            fArr.put(i.toJSONObj());
        }

        JSONArray sArr = new JSONArray();
        for(Schedule.startStop i : Saturday){
            sArr.put(i.toJSONObj());
        }

        JSONArray suArr = new JSONArray();
        for(Schedule.startStop i : Sunday){
            suArr.put(i.toJSONObj());
        }

        try {
        obj.put("Monday",mArr);
        obj.put("Tuesday",tArr);
        obj.put("Wednesday",wArr);
        obj.put("Thursday",thArr);
        obj.put("Friday",fArr);
        obj.put("Saturday",sArr);
        obj.put("Sunday", suArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public void deleteAll() {
        Monday = null;
        Tuesday = null;
        Wednesday = null;
        Thursday = null;
        Friday = null;
        Saturday = null;
        Sunday = null;

        Monday = new ArrayList<>();
        Tuesday = new ArrayList<>();
        Wednesday = new ArrayList<>();
        Thursday = new ArrayList<>();
        Friday = new ArrayList<>();
        Saturday = new ArrayList<>();
        Sunday = new ArrayList<>();
    }

    @NonNull
    @Override
    public String toString() {
        String str = "";

        str += "Sunday: \n";
        for (startStop i : Sunday)
        {
           str += "\t"+i.toString()+"\n";
        }
        str += "Monday: \n";
        for (startStop i : Monday)
        {
            str += "\t"+i.toString()+"\n";
        }
        str += "Tuesday: \n";
        for (startStop i : Tuesday)
        {
            str += "\t"+i.toString()+"\n";
        }
        str += "Wednesday: \n";
        for (startStop i : Wednesday)
        {
            str += "\t"+i.toString()+"\n";
        }
        str += "Thursday: \n";
        for (startStop i : Thursday)
        {
            str += "\t"+i.toString()+"\n";
        }
        str += "Friday: \n";
        for (startStop i : Friday)
        {
            str += "\t"+i.toString()+"\n";
        }
        str += "Saturday: \n";
        for (startStop i : Saturday)
        {
            str += "\t"+i.toString()+"\n";
        }
        return str;
    }
}
