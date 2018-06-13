package com.example.dannylo.bluetoothsensor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final int REQUEST_ENABLE_BT = 2;
    private TextView tempConfig;
    private Button buttonCancel;
    private Button visibleButton;
    private Button configureNew;
    private EditText tempEdit;

    private AcceptConnection acceptConnection;
    private BluetoothAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempConfig = findViewById(R.id.temperatureValue);
        buttonCancel = findViewById(R.id.cancelCon);
        visibleButton = findViewById(R.id.visible);
        configureNew = findViewById(R.id.configureTemp);
        tempEdit = findViewById(R.id.newTempEdit);

        visibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.this.adapter.isEnabled()) {
                    Intent discoverableIntent = new
                            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 400);
                    startActivity(discoverableIntent);
                    Toast.makeText(MainActivity.this, "Device discoverable for 400 seconds.", Toast.LENGTH_SHORT).show();
                    acceptConnection = new AcceptConnection(adapter, MainActivity.this);
                    acceptConnection.start();


                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.cancelConnection();
            }
        });

        configureNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempConfig.setText(tempEdit.getText().toString());
                tempEdit.setText("");
                Toast.makeText(MainActivity.this, "Temperature is setted.", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter == null){
            Toast.makeText(this, "Bluetooth does not exist.", Toast.LENGTH_SHORT);
        } else {
            if (!adapter.isEnabled()) {
                //habilitando bluetooth no device.
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }



    }

    public void cancelConnection(){
        this.acceptConnection.cancelConnection();
        Toast.makeText(this, "Connection has been canceled.", Toast.LENGTH_SHORT).show();
    }

    public void publishResult(String message){
        System.out.println("MENSAGEM RECEBIDA! "+ message);
        acceptConnection.cancelConnection();
        tempConfig.setText(message.trim());
        Toast.makeText(MainActivity.this, "Temperature is setted.", Toast.LENGTH_SHORT).show();
    }
}
