package com.bpm202.SensorProject.Main.Exercise;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm202.SensorProject.Data.ExDataSrouce;
import com.bpm202.SensorProject.Data.ExRepository;
import com.bpm202.SensorProject.Data.ExVo;
import com.bpm202.SensorProject.GraphSendTask;
import com.bpm202.SensorProject.Main.Temp.ManagerBLE;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;
import com.bpm202.SensorProject.db.SendScheduleData;

import java.util.List;
import java.util.UUID;

public class MainExerciseActivity extends AppCompatActivity {

    private final static String TAG = "MainExerciseActivity";

    private Handler mHandler = new Handler();

    private BluetoothGatt gatt;

    private boolean isRunning = false;
    private Button btnInfoName;

    private ScheduleValueObject scheduleVo;
    private FrameLayout fl_ready;
    private FrameLayout fl_start;
    private TextView tv_count;
    private ImageView iv_info_image;
    private TextView tv_kg;
    private TextView tv_remaining;
    private TextView tv_set;
    private TextView tv_rest;
    private TextView tv_start_count;
    private ImageView iv_start_word;
    private TextView tv_remaining_label;

    private int scheduleVoCount;
    private int scheduleVoSetCount;
    private int scheduleVoRest;
    private boolean isRestTime = false;
    private TextView tv_rest_time_label;
    private ImageView iv_now_img;
    private boolean isFinished = false;
    private long startTime;
//    private boolean isDone = true;

    @Override
    public void onBackPressed() {
        isRunning = false;
        failCountToConnect = 0;
        isRestTime = false;
        isFinished = true;

        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_main);
        mExStartTime = System.currentTimeMillis();

        scheduleVo = (ScheduleValueObject) getIntent().getSerializableExtra("ScheduleValueObject");
        init();

        //        sendData();
//        scheduleVoCount = 1;
//        scheduleVoSetCount = 0;
//        mcheckingTimeHandler.sendEmptyMessageDelayed(400, 1);
    }

    private void init() {
        btnInfoName = findViewById(R.id.btn_info_name);
        iv_info_image = findViewById(R.id.iv_info_image);


        tv_kg = findViewById(R.id.tv_kg);
        tv_remaining = findViewById(R.id.tv_remaining);

        tv_set = findViewById(R.id.tv_set);
        tv_rest = findViewById(R.id.tv_rest);

        tv_remaining_label = findViewById(R.id.tv_remaining_label);

        if (MappingUtil.isCountType(this, scheduleVo.getType().getName())) {
            tv_remaining_label.setText(R.string.schedules_count);
        } else if (MappingUtil.isWaitingExercise(scheduleVo.getType())) {
            tv_remaining_label.setText(R.string.schedules_duration);
        } else {
            tv_remaining_label.setText(R.string.schedules_count);
        }

        tv_kg.setText(String.format("%02d", scheduleVo.getWeight()));
        scheduleVoCount = scheduleVo.getCount();
        tv_remaining.setText(String.format("%02d", scheduleVoCount));
        scheduleVoSetCount = scheduleVo.getSetCnt();
        tv_set.setText(String.format("%02d", scheduleVoSetCount));
        scheduleVoRest = scheduleVo.getRest();
        tv_rest.setText(String.format("%02d", scheduleVoRest));

        iv_info_image.setImageDrawable(getResources().getDrawable(MappingUtil.exerciseIconResourceBig[scheduleVo.getType().getId() - 1]));
        btnInfoName.setText(MappingUtil.name(this, scheduleVo.getType().getName()));

        fl_ready = findViewById(R.id.fl_ready);
        fl_start = findViewById(R.id.fl_start);
        tv_count = findViewById(R.id.tv_count);
        tv_start_count = findViewById(R.id.tv_start_count);
        iv_start_word = findViewById(R.id.iv_start_word);
        tv_rest_time_label = findViewById(R.id.tv_rest_time_label);

        iv_now_img = findViewById(R.id.iv_now_img);
        changeReady(true);

        firstData = 0;
        Message msg = new Message();
        msg.what = 1000;
        msg.arg1 = 5;
        mReadyHandler.sendMessageDelayed(msg, 1000);

    }

    private Handler mRestTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 300:
                    int n = msg.arg1;
                    if (n == 0) {
                        tv_rest_time_label.setVisibility(View.GONE);
                        msg = new Message();
                        msg.what = 1000;
                        msg.arg1 = 5;
                        mReadyHandler.sendMessage(msg);
                        break;
                    } else {
                        msg = new Message();
                        msg.what = 300;
                        msg.arg1 = --n;
                        mRestTimeHandler.sendMessageDelayed(msg, 1000);
                    }
                    break;
            }
        }
    };

    private final Handler mReadyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    int n = msg.arg1;
                    if (!isRestTime) {
                        if (n == 5) {
                            scanLeDevice(true);
                        }
                    }

                    if (n > 0) {
                        changeReady(true);
                        tv_count.setText(String.valueOf(n));

                        msg = new Message();
                        msg.what = 1000;
                        msg.arg1 = --n;
                        mReadyHandler.sendMessageDelayed(msg, 1000);
                    } else {
                        changeReady(false);
                        iv_start_word.setVisibility(View.VISIBLE);

                        if (!isRunning) {
                            isRunning = true;
                        } else if (isRestTime) {
                            isRestTime = false;
                        }
                    }
                    break;
            }
        }
    };


    private void changeReady(boolean isReadyShown) {
        if (isReadyShown) {
            fl_ready.setVisibility(View.VISIBLE);
            fl_start.setVisibility(View.GONE);
        } else {
            fl_ready.setVisibility(View.GONE);
            fl_start.setVisibility(View.VISIBLE);
        }
    }


    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice(final boolean enable) {
        if (enable) { // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ManagerBLE.Instance().getbLEScanner().stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);
            ManagerBLE.Instance().getbLEScanner().startScan(mScanCallback);
        } else {
            ManagerBLE.Instance().getbLEScanner().startScan(mScanCallback);
        }
    }

    private String mDeviceName;
    private String mDeviceAddress;

    private int failCountToConnect = 0;

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
                        if (result != null && hasDevice(result)) {
                            ManagerBLE.Instance().getbLEScanner().stopScan(mScanCallback);
                            gatt = mDevice.connectGatt(MainExerciseActivity.this, true, mGattCallback);
                        }
                    }
                }
            });
        }
    };

    private boolean hasDevice(ScanResult result) {
        return result.getDevice() != null && result.getDevice().getName() != null && !result.getDevice().getName().isEmpty() && result.getDevice().getName().equals("sensor");
    }

    public static final String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public static final String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public static final String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public static final String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";

    public static final String EXTRA_DATA_STRING = "com.example.bluetooth.le.EXTRA_DATA_STRING";
    public static final String EXTRA_DATA_RAW = "com.example.bluetooth.le.EXTRA_DATA_RAW";
    public static final String UUID_STRING = "com.example.bluetooth.le.UUID_STRING";
    public static final String UUID_INTENT = "com.example.bluetooth.le.UUID_INTENT";

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
//        getActivity().sendBroadcast(intent);
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

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Log.d("Test", "mGattUpdateReceiver onReceive  : " + action);
            if (ACTION_GATT_CONNECTED.equals(action)) {
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                //서비스를 제공하고 있는지를 보여준다.
                if (gatt != null) {
                    discoveredGattServices(gatt.getServices());
                }
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                if (!isRunning) {
                    return;
                }
                byte[] data = intent.getByteArrayExtra(EXTRA_DATA_RAW);
                String data_string = intent.getStringExtra(EXTRA_DATA_STRING);
                String uudi_data = intent.getStringExtra(UUID_STRING);
                dataReceived(uudi_data, data_string, data);
            }
        }
    };

    private int RangeCount = 0;

    // for radian -> dgree
    private double RAD2DGR = 180 / Math.PI;
    private static final float NS2S = 1.0f / 1000000000.0f;

    //Roll and Pitch
    private double pitch;
    private double roll;
    private double yaw;

    //timestamp and dt
    private double timestamp;
    private double dt;

    private int firstData = 0;
    private boolean isMovedDone = false;

    public void dataReceived(String uudi_data, String data_string, byte[] row_data) {
        if (isRestTime || isFinished) {
            return;
        }

        int stx = getClearDataFromByte(row_data[0]);
        int seq = getClearDataFromByte(row_data[1]);

        short Gyro_X = getConvertData(row_data[2], row_data[3]);
        short Gyro_Y = getConvertData(row_data[4], row_data[5]);
        short Gyro_Z = getConvertData(row_data[6], row_data[7]);
        short Acc_X = getConvertData(row_data[8], row_data[9]);
        short Acc_Y = getConvertData(row_data[10], row_data[11]);
        short Acc_Z = getConvertData(row_data[12], row_data[13]);
        short range = getConvertData(row_data[14], row_data[15]);
        int batt = row_data[16];
        int etx = row_data[17];

        float tempX = getFloatFromData(Acc_X) - getFloatFromData(Gyro_X);
        float tempY = getFloatFromData(Acc_Y) - getFloatFromData(Gyro_Y);
        float tempZ = getFloatFromData(Acc_Z) - getFloatFromData(Gyro_Z);

        double angleXZ = Math.abs((Math.atan2(tempX, tempZ) * 180 / Math.PI) - 180);
        double angleYZ = Math.abs(Math.atan2(tempY, tempZ) * 180 / Math.PI) - 180;


        /* 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)을 구한다.
         * dt : 센서가 현재 상태를 감지하는 시간 간격
         * NS2S : nano second -> second */
        dt = (System.currentTimeMillis() - timestamp) * NS2S;
        timestamp = System.currentTimeMillis();


        /* 맨 센서 인식을 활성화 하여 처음 timestamp가 0일때는 dt값이 올바르지 않으므로 넘어간다. */

        if (dt - timestamp * NS2S != 0) {

            /* 각속도 성분을 적분 -> 회전각(pitch, roll)으로 변환.
             * 여기까지의 pitch, roll의 단위는 '라디안'이다.
             * SO 아래 로그 출력부분에서 멤버변수 'RAD2DGR'를 곱해주어 degree로 변환해줌.  */
            pitch = pitch + Gyro_Y * dt;
            roll = roll + Gyro_X * dt;
            yaw = yaw + Gyro_Z * dt;
        }

        if (MappingUtil.isUsingRangeType(this, scheduleVo.getType().getName())) {
            if (firstData == 0) {
                firstData = (int) range;
                startTime = System.currentTimeMillis();
            }

            if (!isMovedDone && (firstData - (int) range) > 30) {
                isMovedDone = true;
            }

            if (isMovedDone && (firstData - (int) range) <= 5) {
                isMovedDone = false;
                if (scheduleVoSetCount != 0) {
                    mcheckingTimeHandler.removeMessages(400);
                    mcheckingTimeHandler.sendEmptyMessageDelayed(400, 1);
                } else {
                    isFinished = true;
                    onBackPressed();
                }
            }
        } else {

            if (firstData == 0) {
                firstData = (int) angleYZ;
                startTime = System.currentTimeMillis();
            }

            int absAngleYZ = Math.abs((firstData - (int) angleYZ));

            if (!isMovedDone && absAngleYZ > 60) {
                isMovedDone = true;
            }

            if (isMovedDone && absAngleYZ <= 5) {
                isMovedDone = false;
                if (scheduleVoSetCount != 0) {
                    mcheckingTimeHandler.removeMessages(400);
                    mcheckingTimeHandler.sendEmptyMessageDelayed(400, 1);
                } else {
                    isFinished = true;
                    onBackPressed();
                }
            }
        }
    }

    private Handler mcheckingTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 400:
                    int tempCount = scheduleVoCount - (++RangeCount);
                    if (tempCount != 0) {
                        iv_start_word.setVisibility(View.GONE);
                        tv_start_count.setVisibility(View.VISIBLE);
                        iv_now_img.setVisibility(View.VISIBLE);
                        tv_start_count.setText(String.valueOf(tempCount));
                    } else {
                        scheduleVoSetCount--;
                        RangeCount = 0;

                        scheduleVo.setSetCnt(scheduleVoSetCount);
                        exercisePost(scheduleVo, scheduleVoSetCount - scheduleVo.getSetCnt());
                        if (scheduleVoSetCount > 0) {

                            tv_rest_time_label.setVisibility(View.VISIBLE);
                            iv_now_img.setVisibility(View.GONE);
                            tv_start_count.setVisibility(View.GONE);
                            Message msg1 = new Message();
                            msg1.what = 300;
                            msg1.arg1 = scheduleVoRest;
                            mRestTimeHandler.sendMessage(msg1);
                            isRestTime = true;
                        } else {
                            iv_now_img.setVisibility(View.GONE);
                            tv_start_count.setVisibility(View.GONE);
                            isFinished = true;

                            scheduleVo.setSuccess(true);
//                            notifyDataSetChanged();
//                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.FINISH);
//                            float startPos1 = statusLayout.getHeight();
//                            tvDesc.setText(getString(R.string.play_good));
//                            tvDesc.setVisibility(View.VISIBLE);
//                            statusLayout.animate().translationY(startPos1);
//                            mCircleView.setImageDrawable(null);



                            sendData();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainExerciseActivity.this);
                            builder.setTitle(scheduleVo.getType().getName() + "을 끝냈습니다.");
                            builder.setMessage("오늘의 운동으로 이동 하겠습니다.");

                            builder.setCancelable(false);
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            });
                            builder.show();
                        }
                    }
                    break;
            }
        }
    private void exercisePost(ScheduleValueObject scheduleVo, int set) {
        ExVo.Builder builder = new ExVo.Builder();
        builder.setCount(scheduleVo.getCount());
        builder.setCountMax(scheduleVo.getCount());
        builder.setSetCnt(set);
        builder.setSetMax(scheduleVo.getSetCnt());
        builder.setRest(scheduleVo.getRest());
        builder.setType(scheduleVo.getType());
        builder.setDuration((int) Util.Time.getDuration(startTime));
        ExVo vo = builder.create();

        ExRepository.getInstance().addExercise(vo, new ExDataSrouce.UploadCallback() {
            @Override
            public void onUploaded() {

            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }
    };



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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ManagerBLE.Instance().getBluetoothAdapter() != null && gatt != null) {
            gatt.disconnect();
            gatt = null;
        } else {
            Log.w(TAG, "BluetoothAdapter disconnect");
        }
        firstData = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
//        firstData = 0;
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
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

    // 운동 시작 시간을 저장하기 위한 변수
    private long mExStartTime = 0;

    private void sendData() {
        SendScheduleData data = new SendScheduleData();
        String exName = MappingUtil.name(getApplicationContext(), scheduleVo.getType().getName());
        data.name = exName.replace("\n", "");
        data.day = scheduleVo.getDay().name();
        data.setCnt = scheduleVo.getSetCnt();
        data.count = scheduleVo.getCount();
        data.weight = scheduleVo.getWeight();
        data.rest = scheduleVo.getRest();
        data.sTime = data.getCurrentTime(mExStartTime);
        data.eTime = data.getCurrentTime(System.currentTimeMillis());

        new GraphSendTask(getApplicationContext(), graphSendTaskHandler).execute(data, data);
    }


    Handler graphSendTaskHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = (String) msg.obj;
            str = TextUtils.isEmpty(str) ? "str is null" : str;
            Log.e("str", str);
        }
    };
}
