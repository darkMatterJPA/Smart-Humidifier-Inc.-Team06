package com.example.humidifier;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
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
                System.out.println("thing-Error-notification");
                notificationDialog("Error","Error Something is Wrong");

            }

        }).on("warningNotification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                System.out.println("thing-warningNotification");

                notificationDialog("Low Water Level","Low Water Level Refill soon.");


            }
        }).on("refill-notification", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                notificationDialog("Refill Water Tank","Low Water Level Please Refill Water Tank NOW!!!");

                System.out.println("thing-refill-notification");
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


   // @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String x, String y) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.waterdrop)
                .setContentTitle(x)
                .setContentText(y)
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }
}
