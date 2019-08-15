package com.bpm202.SensorProject.Main.Settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.API.WithDrawAPI;
import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Main.Schedules.WeeksPlanActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.SplashActivity;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.ValueObject.ApiObj;

//public class SettingActivity extends BaseFragment {
public class SettingActivity extends AppCompatActivity {

    private Button btnBottomPlan;
    private Button btnBottomSetting;
    private Button btnBottomRecord;
    private Button btnBottomToday;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting2);

        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, null));
        getSupportActionBar().setTitle(R.string.menu_setting);

        initBottomMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_icon_menu, menu); // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true;
    }

    private void initBottomMenu() {

        btnBottomToday = findViewById(R.id.btn_bottom_today);
        btnBottomToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "today", Toast.LENGTH_SHORT).show();
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
        btnBottomPlan = findViewById(R.id.btn_bottom_plan);
//        btnBottomPlan.setSelected(true);
//        btnBottomPlan.setClickable(false);

        btnBottomPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "plan", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(true);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(false);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(true);

                Intent intent = new Intent(SettingActivity.this, WeeksPlanActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
//        btnBottomPlan.setClickable(false);

        btnBottomRecord = findViewById(R.id.btn_bottom_record);
        btnBottomRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "record", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(true);
                btnBottomSetting.setSelected(false);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(false);
                btnBottomSetting.setClickable(true);
            }
        });
        btnBottomSetting = findViewById(R.id.btn_bottom_setting);
        btnBottomSetting.setSelected(true);
        btnBottomSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "setting", Toast.LENGTH_SHORT).show();
                btnBottomToday.setSelected(false);
                btnBottomPlan.setSelected(false);
                btnBottomRecord.setSelected(false);
                btnBottomSetting.setSelected(true);

                btnBottomToday.setClickable(true);
                btnBottomPlan.setClickable(true);
                btnBottomRecord.setClickable(true);
                btnBottomSetting.setClickable(false);

                Intent intent = new Intent(SettingActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }

        });
        btnBottomSetting.setClickable(false);

    }

    /*
    private Button btn_password;
    private Button btn_my_infomation;
    private Button btn_rule;
    private Button btn_log_out;
    private Button btn_withdraw;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting2;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        btn_password = v.findViewById(R.id.btn_password);
        btn_my_infomation = v.findViewById(R.id.btn_my_infomation);
        btn_rule = v.findViewById(R.id.btn_rule);
        btn_log_out = v.findViewById(R.id.btn_log_out);
        btn_withdraw = v.findViewById(R.id.btn_withdraw);

        initListener();
    }

    private void initListener() {
        btn_password.setOnClickListener(null);
        btn_my_infomation.setOnClickListener(null);
        btn_rule.setOnClickListener(null);
        btn_log_out.setOnClickListener(onClickLogoutListener);
        btn_withdraw.setOnClickListener(onClickWithDrawListener);
    }

    private View.OnClickListener onClickLogoutListener = v -> {
        new AppPreferences(getContext()).setStringPref(AppPreferences.KEY_TOKEN, "");
        new AppPreferences(getContext()).setStringPref(AppPreferences.KEY_AUTO_LOGIN, false);
        new AppPreferences(getContext()).setStringPref(AppPreferences.KEY_SAVE_ACCOUNT, "");
        new AppPreferences(getContext()).setStringPref(AppPreferences.KEY_CHECKED_BUTTON_ACCOUNT, false);

        activityFinishToSplash();
    };

    private View.OnClickListener onClickWithDrawListener = v -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.popup_title_secession);
        builder.setMessage(R.string.popup_contents_secession);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String token = new AppPreferences(getContext()).getStringPref(AppPreferences.KEY_TOKEN);
            WithDrawAPI.secession(token,
                    callback -> {
                        ApiObj apiObj = (ApiObj) callback;
                        if (apiObj.status.equals(Api.STATUS_OK)) {
                            QToast.showToast(getContext(), R.string.menu_secession);
                            dialog.dismiss();
                            activityFinishToSplash();
                        }
                    });
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    };

    private void activityFinishToSplash() {
        Intent intent = new Intent(getContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
}
