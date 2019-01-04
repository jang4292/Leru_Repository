package com.bpm.bpm_ver4;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceScanActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = DeviceScanActivity.class.getSimpleName();

    // 리스트뷰에 적용할 어뎁터 클래스
    private LeDeviceListAdapter mLeDeviceListAdapter;
    // 시스템의 블루투스 어뎁터
    private BluetoothAdapter mBluetoothAdapter;
    // 스캔중인지 상태 저장
    private boolean isScanning;
    // 스캔정지 콜백을 받을 핸들러
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000; // 10초

    // TODO: 2018-11-09 : 뷰 처리 필요
    // view
    private ListView mPairedDeviceListview;
    private ListView mNewDeviceListView;
    private Button mScanButton;


    private void initView() {
        mNewDeviceListView = findViewById(R.id.new_devices);
        mPairedDeviceListview = findViewById(R.id.paired_devices);
        mScanButton = findViewById(R.id.button_scan);

        mNewDeviceListView.setOnItemClickListener(this);
//        mPairedDeviceListview.setOnItemClickListener(this);
        mScanButton.setOnClickListener(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        initView();
        mHandler = new Handler();

        // 블루투스 사용가능 스마트폰인지 확인
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE 지원 안함", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 블루투스 매니저에서 어뎁터를 가져오기위해 시스템에서 매니저를 얻음
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter(); // 블루투스 어뎁터를 얻음

        // 블루투스 어뎁터가 없으면 종료
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스 지원 안함", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 블루투스가 꺼졌으면 블루투스를 키는 인텐트를 띄운다.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        // 리스트뷰에 디바이스들을 표시할 어뎁터 생성및 적용
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mNewDeviceListView.setAdapter(mLeDeviceListAdapter);
        // 스캔시작
        scanLeDevice(true);
    }


    // 인텐트를 돌려받았을때 블루투스를 허용하지 않으면 액티비티 종료
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 화면밖으로 나가면 블루투스 스캔 종료, 리스트뷰의 아이템들 제거
    @Override
    protected void onPause() {
        super.onPause();
        // 스캔종료
        scanLeDevice(false);
        // 아이템 초기화
        mLeDeviceListAdapter.clear();
    }


    // 리스트에서 아이템을 선택하면 선택된 디바이스 정보를 호출한(액티비티)로 넘김
    // TODO: 2018-11-09 : 디바이스 정보를 메인화면에 넘기는 추가 작업 필요
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        final Intent intent = new Intent();
        intent.putExtra(Common.EXTRA_DEVICE_NAME, device.getName());
        intent.putExtra(Common.EXTRA_DEVICE_ADDRESS, device.getAddress());
        if (isScanning) {
            mBluetoothAdapter.stopLeScan(mLeScancallback);
            isScanning = false;
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_scan) {
            mLeDeviceListAdapter.clear();
            scanLeDevice(true);
        }
    }


    // 블루투스 장치 찾기 및 중지
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScancallback);
                }
            }, SCAN_PERIOD);

            isScanning = true;
            mBluetoothAdapter.startLeScan(mLeScancallback);
        } else {
            isScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScancallback);
        }
    }




    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices; // 블루투스 장치들을 저장할 List
        private LayoutInflater mInflater;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflater = DeviceScanActivity.this.getLayoutInflater();
        }

        // 디바이스 추가, List뷰에 이미 있는것은 추가하지 않음
        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        // 디바이스를 반환
        private BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        // List 내용들을 전부 제거
        private void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return mLeDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_device, null);
                viewHolder = new ViewHolder();
                viewHolder.tvName = convertView.findViewById(R.id.device_name);
                viewHolder.tvAddress = convertView.findViewById(R.id.device_address);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            BluetoothDevice device = getItem(position);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.tvName.setText(deviceName);
            } else {
                viewHolder.tvName.setText("Unknown device");
            }
            viewHolder.tvAddress.setText(device.getAddress());
            return convertView;
        }
    }


    // 블루투스 디바이스를 찾았을때 이벤트
    private BluetoothAdapter.LeScanCallback mLeScancallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };


    // TODO: 2018-11-09 : 뷰홀더 생성
    private class ViewHolder {
        TextView tvName;
        TextView tvAddress;
    }
}























