package com.bpm.bpm_ver4.exercise;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.CommonUrl;
import com.bpm.bpm_ver4.databinding.ActivityMainBinding;
import com.bpm.bpm_ver4.exercise.play.PlayFragment;
import com.bpm.bpm_ver4.service.BluetoothLeService;
import com.bpm.bpm_ver4.util.ActivityUtils;
import com.bpm.bpm_ver4.util.view.ActionToggleUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private long mLastClick = 0;
    private final long CLICK_DELAY = 2000;

    private ActionToggleUtil toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.layoutAppbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.layoutAppbar.tvTitleToolbar.setText(R.string.menu_exercise);

        toggle = new ActionToggleUtil(
                this, binding.navView,
                binding.drawerLayout, binding.layoutAppbar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        PlayFragment playFragment = PlayFragment.newInstance();
        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                playFragment, R.id.fragment_container);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.setCheck(R.id.nav_play);
        View haderView = binding.navView.getHeaderView(0);
        ImageView ivProfile = haderView.findViewById(R.id.iv_profile_image);
        TextView tvNick = haderView.findViewById(R.id.tv_nick);

        int profileRes = R.drawable.ic_account_circlel;
        tvNick.setText(App.getMember().getInfo().getNickname());
        Glide.with(this)
                .load(CommonUrl.profileImageUrl + App.getMember().getInfo().getPhoto())
                .apply(new RequestOptions()
                        .placeholder(profileRes)
                        .error(profileRes)
                        .circleCrop())
                .into(ivProfile);


        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
    }

    @Override
    public void onBackPressed() {
        /* 네비게이션 드로우 닫고 RETURN */
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        /* 클릭 딜레이 2000ms 연속 클릭시 여기까지 실행되고 RETURN */
        if (mLastClick < System.currentTimeMillis() - CLICK_DELAY) {
            mLastClick = System.currentTimeMillis();
            showToast(R.string.exit_button);
            return; // RETURN;
        }

        /* 모든 조건에 맞지 않으면 실행 */
        super.onBackPressed();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public int getToolbarHeight() {
        return binding.layoutAppbar.toolbar.getHeight();
    }

    public void setupTitle(String title) {
        binding.layoutAppbar.tvTitleToolbar.setText(title);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showToast(TAG + "\n"
                    + data.getStringExtra(Common.EXTRA_DEVICE_NAME)
                    + "\n" + data.getStringExtra(Common.EXTRA_DEVICE_ADDRESS));

            if (mServiceConnection != null) {
                unbindService(mServiceConnection);
                mServiceConnection = null;
            }


            mDeviceAddress = data.getStringExtra(Common.EXTRA_DEVICE_ADDRESS);
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                    if (!mBluetoothLeService.initialize()) {
                        Log.e(TAG, "Unable to initialize Bluetooth");
                    }

                    mBluetoothLeService.connect(mDeviceAddress);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mBluetoothLeService = null;
                }
            };
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class); // 인텐트 서비스 생성
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE); // 서비스를 바인딩함
        }
    }

    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    private ServiceConnection mServiceConnection;

    private boolean mConnected;
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                setGattService(mBluetoothLeService.getSupportedGattServices());

            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String text = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                updateConnectionState(text);
                Log.d(TAG, text);
            }
        }
    };


    private void setGattService(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> characteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
        }
    }

    private void updateConnectionState(String text) {
        runOnUiThread(() -> {
            setupTitle(text);
        });
    }

    private void updateConnectionState(int textRes) {
        runOnUiThread(() -> {
            setupTitle(getString(textRes));
            showToast(getString(textRes));
            Log.i(TAG, getString(textRes));
        });
    }


    private IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}




























