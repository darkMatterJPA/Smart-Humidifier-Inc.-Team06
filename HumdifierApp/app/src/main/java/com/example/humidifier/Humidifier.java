package com.example.humidifier;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Humidifier extends Service implements BmService {
    // Binder given to clients
     IBinder binder = new LocalBinder();

     String powerStatus;
     String humidityLevel; // make arraylist
     Schedule schedule;
     String actualHumidity;
     String waterLevel; // make arraylist

    protected Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://polar-meadow-51053.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }

    public JSONObject getHumidityLevel() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("HumidityLevel", powerStatus);

        return obj;
    }


    public JSONObject getPowerStatus() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("power", powerStatus);

        return obj;
    }

//    public JSONObject getPowerStatous() throws JSONException {
//        JSONObject obj = new JSONObject();
//        obj.put("power", powerStatous);
//
//        return obj;
//    }

    @Override
    public void onCreate() {
        super.onCreate();


        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
               // mSocket.emit("foo", "hi");
               // Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

            }

        });
        mSocket.connect();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LocalBinder(){

        }
         Humidifier getService() {
            // Return this instance of LocalService so clients can call public methods
            return Humidifier.this;
        }
    }
}
