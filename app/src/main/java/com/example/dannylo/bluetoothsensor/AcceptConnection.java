package com.example.dannylo.bluetoothsensor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.UUID;

public class AcceptConnection extends Thread {

    private final BluetoothServerSocket bluetoothServerSocket;
    private ManageConnection manageConnection;

    private final String NAME = "SENSOR_SIMULATE";
    private final UUID UUID_APP = UUID.nameUUIDFromBytes(NAME.getBytes());
    private MainActivity mainActivity;

    public AcceptConnection(BluetoothAdapter bluetoothAdapter, MainActivity mainActivity){
        BluetoothServerSocket tmp = null;
        try {
            tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, UUID_APP);
        } catch (IOException e) { }

        bluetoothServerSocket = tmp;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        BluetoothSocket socket = null;

        while(true){
            try {
                socket = bluetoothServerSocket.accept();
                System.out.println("Socket: "+ socket);
            }catch (IOException e){
                break;
            }

            if(socket != null){
                System.out.println("Socket instanciado...");
                try {
                    manageConnection = new ManageConnection(socket, mainActivity);
                    manageConnection.start();
                    bluetoothServerSocket.close();
                } catch (IOException e){
                    Log.d("BUG", e.getMessage());
                }
                break;
            }
        }
    }

    public void cancelConnection(){
        this.manageConnection.cancel();
    }
}
