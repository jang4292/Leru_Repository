package com.bpm202.SensorProject.Main.Exercise;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.CustomView.CircleView;
import com.bpm202.SensorProject.Data.DayOfWeek;
import com.bpm202.SensorProject.Data.ExDataSrouce;
import com.bpm202.SensorProject.Data.ExRepository;
import com.bpm202.SensorProject.Data.ExVo;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.Main.MainDataManager;
import com.bpm202.SensorProject.Main.Schedules.SchedulesFrgment;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.Util.UtilForApp;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ExerciseFrgment extends BaseFragment {

    private final static String TAG = "Test_MainActivity";

    private static final int READY_TIME = 5;
    private static final long COUNT_ANIMATION_DELAY = 200;
    private ReadyTimeUtil mReadyTimeUtil;

    private static ExerciseFrgment instance = null;
    private RecyclerView recyclerView;
    private ConstraintLayout cl_has_exercise;
    private LinearLayout ll_no_exercise;
    private TextView tvDesc;
    private CircleView mCircleView;
    private ConstraintLayout statusLayout;
    private TextView tvWeight;
    private TextView tvCount;
    private TextView tvSet;
    private TextView tvRest;
    private TextView tvWeightLabel;

    private float posX = 0;
    private float posY = 0;


    private float totalAngle;
    private float angle;

    private long startTime;
    private TextView tv_text;
    private Button button;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBLEScanner;
    private Handler mHandler = new Handler();
    private boolean isConnected;
    private BluetoothGatt gatt;


    public static ExerciseFrgment Instance() {
        if (instance == null) {
            instance = new ExerciseFrgment();
        }
        return instance;
    }

    public static void DestroyInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (initBLE()) {
            getActivity().finish();
        } else {
            checkPermissions();
            //initView();
            scanLeDevice(true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            getContext().unregisterReceiver(mGattUpdateReceiver);
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
        if(mBluetoothAdapter != null && gatt != null) {
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
            Log.d(TAG, "[TEST_Data111] action : " + action);
            if (ACTION_GATT_CONNECTED.equals(action)) {
                isConnected = true;
                Log.i(TAG, "CONNECTED");
            } else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                isConnected = false;
                Log.i(TAG, "DISCONNECTED");
            } else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //Log.d(TAG, "TEST onReceive _ ACTION_GATT_SERVICES_DISCOVERED");
                // Show all the supported services and characteristics on the user interface.
                //서비스를 제공하고 있는지를 보여준다.
                discoveredGattServices(gatt.getServices());
            } else if (ACTION_DATA_AVAILABLE.equals(action)) {
                if (!isRunning) {
                    return;
                }
                byte[] data = intent.getByteArrayExtra(EXTRA_DATA_RAW);        //�����Ͱ� ������ �б�
                String data_string = intent.getStringExtra(EXTRA_DATA_STRING);    //�����Ͱ� ��Ʈ������ ��ȯ�ؼ� �б�
                String uudi_data = intent.getStringExtra(UUID_STRING);    //UUID�� ��Ʈ������ ��ȯ�ؼ� �б�
                dataReceived(uudi_data, data_string, data);
            }
        }
    };

    private int RangeCount = 0;
    private boolean isCheckedCountRange = false;

    public void dataReceived(String uudi_data, String data_string, byte[] row_data) {

//        int stx = row_data[0] & 0x00FF;
//        int seq = row_data[1] & 0x00FF;

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

        if (!isCheckedCountRange && Range > 0 && Range < 10) {
            isCheckedCountRange = true;
        } else if (isCheckedCountRange && Range > 30) {
            isCheckedCountRange = false;
            tvWeight.setText(RangeCount++ + "");
        }

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
        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), "BLE 지원 안함", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // 블루투스 매니저에서 어뎁터를 가져오기위해 시스템에서 매니저를 얻음
            final BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter(); // 블루투스 어뎁터를 얻음
            if (mBluetoothAdapter == null) { // 블루투스 어뎁터가 없으면 종료
                Toast.makeText(getContext(), "블루투스 지원 안함", Toast.LENGTH_SHORT).show();
                return true;
            }

            mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (mBLEScanner == null) { // Checks if Bluetooth LE Scanner is available.
                Toast.makeText(getContext(), "Can not find BLE Scanner", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 10;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    BluetoothDevice mDevice = result.getDevice();
                    mDeviceName = result.getDevice().getName();
                    mDeviceAddress = result.getDevice().getAddress();

                    if (mDeviceName != null) {
                        if (result != null && result.getDevice() != null && result.getDevice().getName() != null && !result.getDevice().getName().isEmpty() && result.getDevice().getName().equals("sensor")) {
                            Toast.makeText(getContext(), "[DeviceName] : " + mDeviceName + "\n[DeviceAddress] : " + mDeviceAddress, Toast.LENGTH_SHORT).show();
                            mBLEScanner.stopScan(mScanCallback);
                            gatt = mDevice.connectGatt(getContext(), true, mGattCallback);
                        }
                    }
                }
            });
        }
    };

    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG, "TEST, BluetoothGatt [onConnectionStateChange]: gatt " + gatt + " status : " + status + " newState : " + newState);
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
        Log.i(TAG, "park_Blue broadcastUpdate()0 : ");
        Intent intent = new Intent(action);
        getActivity().sendBroadcast(intent);
    }

    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        //intent.putExtra("com.example.bluetooth.le.UUID_INTENT", characteristic.getValue());
        intent.putExtra(UUID_INTENT, characteristic.getValue());
        Log.d(TAG, "park_Blue_Service broadcastUpdate_0 :" + characteristic.getUuid().toString());
        Log.d(TAG, "TEST park_Blue_Service broadcastUpdate_0 :" + characteristic.getUuid().toString());
        //intent.putExtra("com.example.bluetooth.le.UUID_STRING", characteristic.getUuid().toString());
        intent.putExtra(UUID_STRING, characteristic.getUuid().toString());
        byte[] data = characteristic.getValue();
        Log.d(TAG, "park_Blue_Service broadcastUpdate_1 :" + data.length);
        Log.d(TAG, "TEST park_Blue_Service broadcastUpdate_1 :" + data.length);
        if (data != null && data.length > 0) {
            StringBuilder stringBuilder = new StringBuilder(data.length);
            byte[] var9 = data;
            int var8 = data.length;

            for (int var7 = 0; var7 < var8; ++var7) {
                byte byteChar = var9[var7];
                stringBuilder.append(String.format("%02X ", byteChar));
            }

            //Log.d(TAG, "park_Blue_Service broadcastUpdate_2 :" + stringBuilder.toString());
            Log.d(TAG, "TEST park_Blue_Service broadcastUpdate_2 :" + stringBuilder.toString());
            //intent.putExtra("com.example.bluetooth.le.EXTRA_DATA_STRING", stringBuilder.toString());
            intent.putExtra(EXTRA_DATA_STRING, stringBuilder.toString());
            //intent.putExtra("com.example.bluetooth.le.EXTRA_DATA_RAW", data);
            intent.putExtra(EXTRA_DATA_RAW, data);
        }

        getActivity().sendBroadcast(intent);
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_play;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        recyclerView = v.findViewById(R.id.recycler_view);
        statusLayout = v.findViewById(R.id.constraintLayout);
        tvWeight = v.findViewById(R.id.tv_weight_num);
        tvCount = v.findViewById(R.id.tv_count_num);
        tvSet = v.findViewById(R.id.tv_set_num);
        tvRest = v.findViewById(R.id.tv_rest_num);
        tvDesc = v.findViewById(R.id.tv_desc);
        mCircleView = v.findViewById(R.id.iv_exercise);
        tvWeightLabel = v.findViewById(R.id.tv_weight_label);
        cl_has_exercise = v.findViewById(R.id.cl_has_exercise);
        ll_no_exercise = v.findViewById(R.id.ll_no_exercise);

        tv_text = v.findViewById(R.id.tv_text);
        button = v.findViewById(R.id.ibtn_schedules);

        mReadyTimeUtil = new ReadyTimeUtil();

        int dayCode = Util.CalendarInfo.getCalendar().get(Calendar.DAY_OF_WEEK);
        DayOfWeek dayOfWeek = DayOfWeek.findByCode(dayCode);

        @NonNull List<ScheduleValueObject> todaySchedules = MainDataManager.Instance().getScheduleValueObjectForDay(dayOfWeek);

        if (todaySchedules.size() == 0) {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.today);
            setNoExerciseLayout(true);

            if (MainDataManager.Instance().getListScheduleValueObject().size() == 0) {
                tv_text.setText("나에게 맞는 일정을 잡고 시작하세요");
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(v1 -> {
                    getActivity().setTitle(R.string.menu_schedules);
                    Util.FragmentUtil.replaceFragment(getFragmentManager(), SchedulesFrgment.Instance(), R.id.fragment_container);
                });

            }
        } else {
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.select);
            setNoExerciseLayout(false);

            ExerciseAdapter adpater = new ExerciseAdapter(getContext(), todaySchedules);
            recyclerView.setAdapter(adpater);
            UtilForApp.setDividerItemDecoration(getContext(), recyclerView, R.drawable.divider_shape);
        }
    }

    private void setNoExerciseLayout(boolean isNoData) {
        if (isNoData) {
            cl_has_exercise.setVisibility(View.GONE);
            ll_no_exercise.setVisibility(View.VISIBLE);
        } else {
            cl_has_exercise.setVisibility(View.VISIBLE);
            ll_no_exercise.setVisibility(View.GONE);
        }
    }

    private boolean isRunning = false;


    public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

        private final Context context;
        private List<ScheduleValueObject> list;

        public ExerciseAdapter(Context context, List<ScheduleValueObject> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_play_select, viewGroup, false);
            return new ExerciseViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int position) {
            exerciseViewHolder.onBinding(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list != null ? list.size() : 0;
        }

        class ExerciseViewHolder extends RecyclerView.ViewHolder {

            private CircleView ibtnIcon;
            private TextView tvName;
            private ImageView iv_check;

            public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                ibtnIcon = itemView.findViewById(R.id.ibtn_exercise);
                iv_check = itemView.findViewById(R.id.iv_check);
            }

            private void onBinding(ScheduleValueObject scheduleVo) {

                itemView.setOnTouchListener((v, event) -> {
                    final int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    Log.d("x, y", location[0] + ":" + location[1]);
                    posX = location[0]; // x좌표
                    posY = location[1]; // y좌표
                    return false;
                });
                itemView.setOnClickListener(v -> {

                    if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.READY)) {
                        return;
                    }

                    Rect rect = new Rect();
                    getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                    int statusbarHeight = rect.top;
                    int toolbarHeight = ((MainActivity) getActivity()).getSupportActionBar().getHeight();

                    ImageView viewSrc = new ImageView(context);
                    viewSrc.setImageDrawable(mCircleView.getResources().getDrawable(getIconResource(scheduleVo)));
                    int size = mCircleView.getResources().getDimensionPixelSize(R.dimen.dimen_70);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(size, size);

                    viewSrc.setLayoutParams(params);
                    viewSrc.setX(posX);
                    viewSrc.setY(posY - statusbarHeight - toolbarHeight);

                    ConstraintLayout layout = getView().findViewById(R.id.parent);
                    layout.addView(viewSrc);

                    int[] location = new int[2];
                    mCircleView.getLocationOnScreen(location);
                    float dimen_69dp = context.getResources().getDimension(R.dimen.dimen_69);

                    viewSrc.animate().translationX(location[0] + dimen_69dp).translationY(location[1] - statusbarHeight - toolbarHeight + dimen_69dp)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    viewSrc.animate().setListener(null);
                                    viewSrc.animate().alpha(0f);
                                    mCircleView.setImageDrawable(getResources().getDrawable(getIconResource(scheduleVo)));
                                }
                            });

                    ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.IDLE);

                    float size1 = context.getResources().getDimension(R.dimen.play_circle_size);
                    float stroke = context.getResources().getDimension(R.dimen.play_circle_stroke);

                    mCircleView.setRectResize(size1, stroke);
                    mCircleView.setAngle(0);
                    mCircleView.clearAnimation();
                    mCircleView.invalidate();

                    ((MainActivity) getActivity()).getSupportActionBar().setTitle(MappingUtil.name(context, scheduleVo.getType().getName()));

                    angle = (360f / scheduleVo.getCount());
                    totalAngle = 0;

                    float endPos = 0;
                    float startPos = statusLayout.getHeight();
                    tvDesc.setText(getString(R.string.play_start_by_pressed));
                    statusLayout.animate().translationY(startPos).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            tvCount.setText(String.format("%02d", scheduleVo.getCount()));
                            tvSet.setText(String.format("%02d", scheduleVo.getSetCnt()));
                            tvRest.setText(String.format("%02d", scheduleVo.getRest()));
                            tvWeight.setText(String.format("%02d", scheduleVo.getWeight()));
                            if (scheduleVo.getType().isTime()) {
                                tvWeightLabel.setText(getString(R.string.schedules_rpm));
                            } else {
                                tvWeightLabel.setText(getString(R.string.schedules_kg));
                            }
                            statusLayout.animate().setListener(null);
                            statusLayout.setVisibility(View.VISIBLE);
                            statusLayout.animate().translationY(endPos);

                        }
                    });
                    // 시작 버튼
                    mCircleView.setOnClickListener(btn -> {
                        if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.IDLE)) {
                            Log.d("TEST", "TEST, CircleView Start");

                            tvDesc.setVisibility(View.VISIBLE);
                            tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_48));
                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.READY);
                            tvDesc.setText(String.valueOf(READY_TIME));
                            mReadyTimeUtil.stop();
                            mReadyTimeUtil.setTime(READY_TIME);
                            mReadyTimeUtil.start(time -> {
                                if (time >= 0) {
                                    tvDesc.setText(String.valueOf(time));
                                } else {
                                    ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.RUN);
                                    tvDesc.setVisibility(View.INVISIBLE);
                                    tvDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getResources().getInteger(R.integer.font_size_18));
                                    tvDesc.setText(getString(R.string.play_end_by_pressed));
                                    tvDesc.setVisibility(View.VISIBLE);
                                    startTime = System.currentTimeMillis();
                                    isRunning = true;
                                }
                            });

                        } else if (ExerciseManager.Instance().STATE_CLASS.getCurrentState().equals(ExerciseManager.STATE.RUN)) {
                            isRunning = false;
                            RangeCount = 0;
                            Log.d("TEST", "TEST, CircleView Run");
                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.REST);
                            totalAngle = 360f;

                            mCircleView.startAnimation(() -> totalAngle);
                            int i = scheduleVo.getSetCnt();
                            //TODO to need check, what couunt is.
//                            if (i < 0) {
//                                scheduleVo.setSetCnt(0);
//                            } else {
                            scheduleVo.setSetCnt(--i);
                            //}


                            exercisePost(scheduleVo, i - scheduleVo.getSetCnt());
                            if (scheduleVo.getSetCnt() <= 0) {
                                scheduleVo.setSuccess(true);
                                notifyDataSetChanged();
                                ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.FINISH);

                                /*if (isAllComplete())
                                    view.allFinishSchedules();
                                else*/

                                float startPos1 = statusLayout.getHeight();
                                tvDesc.setText(getString(R.string.play_good));
                                tvDesc.setVisibility(View.VISIBLE);
                                statusLayout.animate().translationY(startPos1);
                                mCircleView.setImageDrawable(null);


                                return;
                            }

                            ExerciseManager.Instance().setSTATE(ExerciseManager.STATE.REST);

                            tvCount.setText(String.format("%02d", scheduleVo.getCount()));
                            tvSet.setText(String.format("%02d", scheduleVo.getSetCnt()));
                            tvRest.setText(String.format("%02d", scheduleVo.getRest()));
                            angle = 360f / (float) scheduleVo.getRest();
                            /*mPlayState.start(
                                    time -> mPlayState.animation(view, angle, mScheduleVo)
                            );*/
                            totalAngle = 0f;


                        } else {
                            Log.d("TEST", "TEST, CircleView nothing");
                        }
                    });
                });

                if (scheduleVo.isSuccess()) {
                    iv_check.setVisibility(View.VISIBLE);
                } else {
                    iv_check.setVisibility(View.GONE);
                }
                ibtnIcon.setImageDrawable(context.getResources().getDrawable(getIconResource(scheduleVo)));
            }

            private int getIconResource(ScheduleValueObject object) {
                return MappingUtil.exerciseIconResource[object.getType().getId() - 1];
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
        }
    }
}
