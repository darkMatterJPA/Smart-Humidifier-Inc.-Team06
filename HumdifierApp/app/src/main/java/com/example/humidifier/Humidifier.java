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

import java.net.URISyntaxException;

public class Humidifier extends Service {
    // Binder given to clients
    private IBinder binder = new LocalBinder();

    protected String powerStatous;
    protected String humidityLevel;
    protected String schedule;
    protected String actualHumidity;
    protected String waterLevel;

    protected Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://polar-meadow-51053.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
               // mSocket.emit("foo", "hi");
               // mSocket.disconnect();
            }

        }).on("waterLevel-to-app", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        }).on("humidityLevel-to-app", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        });
        mSocket.connect();
        if (mSocket.connected()) {
            Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Humidifier getService() {
        return Humidifier.this;
    }


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
         Humidifier getService() {
            // Return this instance of LocalService so clients can call public methods
            return Humidifier.this;
        }
    }
}
