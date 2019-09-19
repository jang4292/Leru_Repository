package com.bpm202.SensorProject.Main.Exercise;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bpm202.SensorProject.Main.History.HistoryCalendarActivity;
import com.bpm202.SensorProject.Main.Schedules.WeeksPlanActivity;
import com.bpm202.SensorProject.Main.Settings.SettingActivity;
import com.bpm202.SensorProject.R;

import java.util.List;
import java.util.UUID;


public class ExerciseActivity extends AppCompatActivity {

    private final static String TAG = "ExerciseActivity";

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBLEScanner;

    private Handler mHandler = new Handler();

    private boolean isConnected;
    private BluetoothGatt gatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initData();

        init();
        /*if (initBLE()) {
            finish();
        } else {
            checkPermissions();
            //initView();
            scanLeDevice(true);
        }*/
    }

    private Button btnBottomPlan;
    private Button btnBottomSetting;
    private Button btnBottomRecord;
    private Button btnBottomToday;

    protected void init() {

        Log.d("Test", "Exercise Activity init");
        setContentView(R.layout.activity_play);
        initView();
//        initTapControl();
//        loadSchedulesData();
        initBottomMenu();
    }

    private void initBottomMenu() {

        btnBottomToday = findViewById(R.id.btn_bottom_today);
        btnBottomToday.setSelected(true);
        btnBottomToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "today", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(true);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(false);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

            }
        });

        btnBottomToday.setClickable(false);


        btnBottomPlan = findViewById(R.id.btn_bottom_plan);
        btnBottomPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "plan", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(true);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(false);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(ExerciseActivity.this, WeeksPlanActivity.class);
                startActivity(intent);
//                overridePendingTransition(0, 0);
                finish();
            }
        });


        btnBottomRecord = findViewById(R.id.btn_bottom_record);
        btnBottomRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "record", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(true);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(false);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(ExerciseActivity.this, HistoryCalendarActivity.class);
                startActivity(intent);
//                overridePendingTransition(0, 0);
                finish();

            }
        });
        btnBottomSetting = findViewById(R.id.btn_bottom_setting);
        btnBottomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(WeeksPlanActivity.this, "setting", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(true);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(false);

                Intent intent = new Intent(ExerciseActivity.this, SettingActivity.class);
                startActivity(intent);
//                overridePendingTransition(0, 0);
                finish();
            }
        });

    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.title_exercise);

    }

    @Override
    public void onResume() {
        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            unregisterReceiver(mGattUpdateReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null && gatt != null) {
            gatt.disconnect();
        } else {
            Log.w(TAG, "BluetoothAdapter disconnect");
        }

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";

    public static final String EXTRA_DATA_STRING = "com.example.bluetooth.le.EXTRA_DATA_STRING";
    public static final String EXTRA_DATA_RAW = "com.example.bluetooth.le.EXTRA_DATA_RAW";
    public static final String UUID_STRING = "com.example.bluetooth.le.UUID_STRING";
    public static final String UUID_INTENT = "com.example.bluetooth.le.UUID_INTENT";

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (ACTION_GATT_CONNECTED.equals(action)) {
                isConnected = true;
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnected = false;
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //서비스를 제공하고 있는지를 보여준다.
                discoveredGattServices(gatt.getServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
/*                if (!isRunning) {
                    return;
                }*/
                byte[] data = intent.getByteArrayExtra(EXTRA_DATA_RAW);
                String data_string = intent.getStringExtra(EXTRA_DATA_STRING);
                String uudi_data = intent.getStringExtra(UUID_STRING);
                dataReceived(uudi_data, data_string, data);
            }
        }
    };

    public void dataReceived(String uudi_data, String data_string, byte[] row_data) {
        int stx = getClearDataFromByte(row_data[0]);
        int seq = getClearDataFromByte(row_data[1]);

        short Gyro_X = getConvertData(row_data[2], row_data[3]);
        short Gyro_Y = getConvertData(row_data[4], row_data[5]);
        short Gyro_Z = getConvertData(row_data[6], row_data[7]);
        short Acc_X = getConvertData(row_data[8], row_data[9]);
        short Acc_Y = getConvertData(row_data[10], row_data[11]);
        short Acc_Z = getConvertData(row_data[12], row_data[13]);
        short Range = getConvertData(row_data[14], row_data[15]);
        int batt = row_data[16];
        int etx = row_data[17];

        /*if (!isCheckedCountRange && Range > 0 && Range < 10) {
            isCheckedCountRange = true;
        } else if (isCheckedCountRange && Range > 30) {
            isCheckedCountRange = false;
            tvWeight.setText(RangeCount++ + "");
        }*/

        StringBuilder sb = new StringBuilder();
        sb.append("[TEST_Data] Gyro [");
        sb.append(getFloatFromData(Gyro_X));
        sb.append(",");
        sb.append(getFloatFromData(Gyro_Y));
        sb.append(",");
        sb.append(getFloatFromData(Gyro_Z));
        sb.append("] Acc [");
        sb.append(getFloatFromData(Acc_X));
        sb.append(",");
        sb.append(getFloatFromData(Acc_Y));
        sb.append(",");
        sb.append(getFloatFromData(Acc_Z));
        sb.append("] Range [");
        sb.append(Range);
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    private float getFloatFromData(short value) {
        int tempPre = value / 100;
        int tempPost = value % 100;
        return tempPre + (float) tempPost / 100;
    }

    private int getClearDataFromByte(byte data) {
        return (int) data & 0x00FF;
    }

    private short getConvertData(byte leftData, byte rightData) {
        int lsb = getClearDataFromByte(leftData);
        int rsb = getClearDataFromByte(rightData);
        return (short) ((lsb << 8) | rsb);
    }

    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void discoveredGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) {
            return;
        }
        String uuid = null;

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATA_AVAILABLE);
        intentFilter.addAction(ACTION_GATT_CONNECTED);
        intentFilter.addAction(ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(ACTION_GATT_SERVICES_DISCOVERED);
        return intentFilter;
    }

    private boolean initBLE() {
        // 블루투스 사용가능 스마트폰인지 확인
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE 지원 안함", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // 블루투스 매니저에서 어뎁터를 가져오기위해 시스템에서 매니저를 얻음
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter(); // 블루투스 어뎁터를 얻음
            if (mBluetoothAdapter == null) { // 블루투스 어뎁터가 없으면 종료
                Toast.makeText(this, "블루투스 지원 안함", Toast.LENGTH_SHORT).show();
                return true;
            }

            mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (mBLEScanner == null) { // Checks if Bluetooth LE Scanner is available.
                Toast.makeText(this, "Can not find BLE Scanner", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 10;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
        }
    }

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable) {
        if (enable) { // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBLEScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);
            mBLEScanner.startScan(mScanCallback);
        } else {
            mBLEScanner.stopScan(mScanCallback);
        }
    }

    private String mDeviceName;
    private String mDeviceAddress;

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            processResult(result);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                processResult(result);
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.d("TEST", "onScanFailed :" + errorCode);
        }

        private void processResult(final ScanResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    BluetoothDevice mDevice = result.getDevice();
                    mDeviceName = result.getDevice().getName();
                    mDeviceAddress = result.getDevice().getAddress();

                    if (mDeviceName != null) {
                        if (result != null && result.getDevice() != null && result.getDevice().getName() != null && !result.getDevice().getName().isEmpty() && result.getDevice().getName().equals("sensor")) {
                            mBLEScanner.stopScan(mScanCallback);
                            gatt = mDevice.connectGatt(ExerciseActivity.this, true, mGattCallback);
                        }
                    }
                }
            });
        }
    };

    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == 2) {
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(intentAction);
                gatt.discoverServices();
            } else if (newState == 0) {
                intentAction = ACTION_GATT_DISCONNECTED;
                broadcastUpdate(intentAction);
            }
        }

        UUID serviceUUID = UUID.fromString("4880c12c-fdcb-4077-8920-a450d7f9b907");
        UUID characteristicUUID = UUID.fromString("fec26ec4-6d71-4442-9f81-55bc21d658d6");

        public UUID convertFromInteger(int i) {
            final long MSB = 0x0000000000001000L;
            final long LSB = 0x800000805f9b34fbL;
            long value = i & 0xFFFFFFFF;
            return new UUID(MSB | (value << 32), LSB);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothGattCharacteristic characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID);
            gatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(convertFromInteger(0x2902));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            BluetoothGattCharacteristic characteristic = gatt.getService(serviceUUID).getCharacteristic(characteristicUUID);
            characteristic.setValue(new byte[]{1, 1});
            gatt.writeCharacteristic(characteristic);
        }

    };

    private void broadcastUpdate(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        intent.putExtra(UUID_INTENT, characteristic.getValue());
        intent.putExtra(UUID_STRING, characteristic.getUuid().toString());
        byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(data.length);
            byte[] var9 = data;
            int var8 = data.length;

            for (int var7 = 0; var7 < var8; ++var7) {
                byte byteChar = var9[var7];
                stringBuilder.append(String.format("%02X ", byteChar));
            }

            Log.d(TAG, "TEST park_Blue_Service broadcastUpdate_2 :" + stringBuilder.toString());
            intent.putExtra(EXTRA_DATA_STRING, stringBuilder.toString());
            intent.putExtra(EXTRA_DATA_RAW, data);
        }

        sendBroadcast(intent);
    }

//    private void initData() {
//        ScheduleRepository repository = ScheduleRepository.getInstance();
//        Util.LoadingProgress.show(this);
//        repository.getSchedules(new ScheduleDataSource.LoadCallback() {
//            @Override
//            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
//                Util.LoadingProgress.hide();
//                MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
//
//                Intent intent = new Intent(ExerciseActivity.this, ExerciseActivity.class);
//                startActivity(intent);
//                /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.Instance()).commit();*/
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                Util.LoadingProgress.hide();
//                Log.e(MainActivity_sub.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
//            }
//        });
//    }


}
