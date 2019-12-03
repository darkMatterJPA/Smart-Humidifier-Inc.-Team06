package com.example.humidifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Humidifier extends Service{
    // Binder given to clients
     IBinder binder = new LocalBinder();

     String powerStatus;
     int humidityLevel;
     Schedule schedule = new Schedule();
     String actualHumidity;
     String waterLevel;

    protected Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://polar-meadow-51053.herokuapp.com/");
        } catch (URISyntaxException e) {
        }
    }

    public JSONObject getHumidityLevel() throws JSONException {

        JSONObject obj = new JSONObject();
        obj.put("Humidity", humidityLevel);

        return obj;
    }


    public JSONObject getPowerStatus() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("power", powerStatus);

        return obj;
    }



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
                //
            }

        }).on("waterLevel-to-app", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("thing-waterLevel-to-app");
                try {
               JSONObject data = new JSONObject((String) args[0]);

                    waterLevel = data.getString("WaterLevel");

                    Intent intent = new Intent("WaterLevel");
                    intent.putExtra("WaterLevel", waterLevel);
                    sendIntent(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).on("humidityLevel-to-app", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                System.out.println("thing-humidityLevel-to-app");

                try {
                    JSONObject data = new JSONObject((String) args[0]);

                    actualHumidity = data.getString("Humidity");

                    Intent intent = new Intent("humidityLevel");
                    intent.putExtra("humidityLevel", actualHumidity);
                    sendIntent(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }).on("Error-notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                //Example of push notification
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle("Error").setContentText("Error Something is Wrong").
                        setContentTitle("Error").setSmallIcon(R.drawable.waterdrop).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
            }

        }).on("warningNotification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle("Low Water Level").setContentText("Low Water Level Refill soon.").
                        setContentTitle("Low Water Level").setSmallIcon(R.drawable.waterdrop).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
            }
        }).on("refill-notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notify=new Notification.Builder
                        (getApplicationContext()).setContentTitle("Refill Water tank").setContentText("Please Refill Water Tank").
                        setContentTitle("Refill Water tank").setSmallIcon(R.drawable.waterdrop).build();

                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                notif.notify(0, notify);
            }
        });
        mSocket.connect();

    }


    private void sendIntent(Intent intent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
