package com.bpm202.SensorProject.Main.Temp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.widget.Toast;

public class ManagerBLE {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bLEScanner;

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public BluetoothLeScanner getbLEScanner() {
        return bLEScanner;
    }

    private static ManagerBLE instance = null;

    public static ManagerBLE Instance() {
        if (instance == null) {
            instance = new ManagerBLE();
        }
        return instance;
    }

    public boolean setBluetoothAdapter(Context context, BluetoothAdapter bleAdapter) {
        bluetoothAdapter = bleAdapter;

//        mBluetoothAdapter = bluetoothManager.getAdapter(); // 블루투스 어뎁터를 얻음
        if (bluetoothAdapter == null) { // 블루투스 어뎁터가 없으면 종료
            Toast.makeText(context, "블루투스 지원 안함", Toast.LENGTH_SHORT).show();
            return false;
        }

        bLEScanner = bluetoothAdapter.getBluetoothLeScanner();
        if (bLEScanner == null) { // Checks if Bluetooth LE Scanner is available.
            Toast.makeText(context, "Can not find BLE Scanner", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

//
//    public void setbLEScanner(Context context,BluetoothLeScanner bLEScanner) {
//        this.bLEScanner = bLEScanner;
//    }
}
