package com.example.humidifier;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.Socket;
import java.net.URISyntaxException;


public class Humidifier {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://polar-meadow-51053.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

}
