package com.bpm202.SensorProject.Main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bpm202.SensorProject.BaseActivity;
import com.bpm202.SensorProject.Data.ScheduleDataSource;
import com.bpm202.SensorProject.Data.ScheduleRepository;
import com.bpm202.SensorProject.Main.History.HistoryFragment;
import com.bpm202.SensorProject.Main.Schedules.SchedulesFrgment;
import com.bpm202.SensorProject.Main.Temp.MainActivity_sub;
import com.bpm202.SensorProject.Main.Temp.MainDataManager;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.ScheduleValueObject;

import java.util.List;

public class MainActivity extends BaseActivity {

    private long mLastClick = 0;
    //    private long lastBottomMenuClick = 0;
    private final long CLICK_DELAY = 2000;
//    private final long MENU_CLICK_DELAY = 2000;

    private Button btnBottomPlan;
    private Button btnBottomSetting;
    private Button btnBottomRecord;
    private Button btnBottomToday;

//    private BluetoothAdapter mBluetoothAdapter;
//    private BluetoothLeScanner mBLEScanner;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

        if (initBLE()) {
            finish();
        } else {
            checkPermissions();

            initView();
            initBottomMenu();
            initData();
        }

    }

    private boolean initBLE() {
        // 블루투스 사용가능 스마트폰인지 확인
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE 지원 안함", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // 블루투스 매니저에서 어뎁터를 가져오기위해 시스템에서 매니저를 얻음
            final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

            if (!ManagerBLE.Instance().setBluetoothAdapter(this, bluetoothManager.getAdapter())) {
                return true;
            }
            /*mBluetoothAdapter = bluetoothManager.getAdapter(); // 블루투스 어뎁터를 얻음
            if (mBluetoothAdapter == null) { // 블루투스 어뎁터가 없으면 종료
                Toast.makeText(this, "블루투스 지원 안함", Toast.LENGTH_SHORT).show();
                return true;
            }*/


//            mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
//            if (mBLEScanner == null) { // Checks if Bluetooth LE Scanner is available.
//                Toast.makeText(this, "Can not find BLE Scanner", Toast.LENGTH_SHORT).show();
//                return true;
//            }
        }
        return false;
    }

    private final int MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE = 10;

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_EXT_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {

        Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fm instanceof MainExerciseFragment) {
            if (mLastClick < System.currentTimeMillis() - CLICK_DELAY) {
                mLastClick = System.currentTimeMillis();
                QToast.showToast(getApplicationContext(), R.string.exit_button);
                return;
            }
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            btnBottomToday.setSelected(true);
            btnBottomToday.setClickable(false);

            btnBottomPlan.setSelected(false);
            btnBottomPlan.setClickable(true);
            btnBottomRecord.setSelected(false);
            btnBottomRecord.setClickable(true);
            btnBottomSetting.setSelected(false);
            btnBottomSetting.setClickable(true);
        }


        super.onBackPressed();
    }

    public void onClickingBtnPlanButton() {
        btnBottomPlan.callOnClick();
    }

    private void initView() {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container, MainExerciseFragment.newInstance()).commit();
//        setTitle(R.string.menu_exercise);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
        });
    }

    private void initData() {
        ScheduleRepository repository = ScheduleRepository.getInstance();
//        Util.LoadingProgress.show(MainActivity_sub.this);
        repository.getSchedules(new ScheduleDataSource.LoadCallback() {
            @Override
            public void onLoaded(List<ScheduleValueObject> scheduleVos) {
//                Util.LoadingProgress.hide();
                MainDataManager.Instance().setListScheduleValueObject(scheduleVos);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.add(R.id.fragment_container, ExerciseFrgment.Instance()).commit();
                fragmentTransaction.add(R.id.fragment_container, MainExerciseFragment.newInstance()).commit();
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                Log.e(MainActivity_sub.TAG, "[SchedulesFragment] getSchedules onDataNotAvailable");
            }
        });

    }

    @Override
    protected void onDestroy() {
        SchedulesFrgment.DestroyInstance();
        HistoryFragment.DestroyInstance();
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initBottomMenu() {


        btnBottomToday = findViewById(R.id.btn_bottom_today);
        btnBottomToday.setSelected(true);
        btnBottomToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "today", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(true);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(false);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

//                setTitle(R.string.menu_exercise);

                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fm instanceof MainExerciseFragment) {
                    return;
                }
                Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), MainExerciseFragment.newInstance(), R.id.fragment_container);

//                Intent intent = new Intent(WeeksPlanActivity.this, ExerciseActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();

            }
        });

        btnBottomToday.setClickable(false);
        btnBottomPlan = findViewById(R.id.btn_bottom_plan);


        btnBottomPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(MainActivity.this, "plan", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(true);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(false);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                setTitle(R.string.menu_schedules);
                if (fm instanceof MainPlanFragment) {
                    return;
                }
                Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), MainPlanFragment.newInstance(), R.id.fragment_container);

//                Intent intent = new Intent(WeeksPlanActivity.this, WeeksPlanActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();
            }
        });
//        btnBottomPlan.setClickable(false);

        btnBottomRecord = findViewById(R.id.btn_bottom_record);
        btnBottomRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, "record", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(true);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(false);
                btnBottomSetting.setClickable(true);

                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                setTitle(R.string.menu_history);
                if (fm instanceof MainHistoryFragment) {
                    return;
                }
                Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), MainHistoryFragment.newInstance(), R.id.fragment_container);


//                Intent intent = new Intent(WeeksPlanActivity.this, HistoryCalendarActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();

            }
        });
        btnBottomSetting = findViewById(R.id.btn_bottom_setting);
        btnBottomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(true);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(false);

                Fragment fm = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                setTitle(R.string.menu_setting);
                if (fm instanceof MainSettingFragment) {
                    return;

                }
                Util.FragmentUtil.replaceFragment(getSupportFragmentManager(), MainSettingFragment.newInstance(), R.id.fragment_container);

//                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                finish();
            }
        });

    }
}
