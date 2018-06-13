package com.example.dannylo.bluetoothsensor;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ManageConnection extends Thread {

    private final BluetoothSocket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private MainActivity main;

    public ManageConnection(BluetoothSocket bluetoothSocket, MainActivity main){
        this.socket = bluetoothSocket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.main = main;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void write(byte[] bytes) {
        try {
            System.out.println("Bytes escritos.");
            outputStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) { }
    }

    @Override
    public void run() {
        final byte[] buffer = new byte[1024];
        int bytes;

        while (true) {
            try {
                // Read from the InputStream
                bytes = inputStream.read(buffer);
                // Send the obtained bytes to the UI activity
                main.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main.publishResult(new String(buffer));
                    }
                });

            } catch (IOException e) {
                break;
            }
        }
    }
}
