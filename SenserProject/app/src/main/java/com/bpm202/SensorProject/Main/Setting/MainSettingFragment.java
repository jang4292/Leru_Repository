package com.bpm202.SensorProject.Main.Setting;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;

public class MainSettingFragment extends BaseFragment {


    private static MainSettingFragment instance = null;

    public static MainSettingFragment newInstance() {
        if (instance == null) {
            instance = new MainSettingFragment();
        }
        return instance;
    }

    private Switch saveId;
    private Switch autoLogin;

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_main;
    }

    @NonNull
    @Override
    protected void initView(View v) {
        ((MainActivity) getActivity()).setTitleText(R.string.menu_setting);
        saveId = v.findViewById(R.id.switch1);
        boolean isAutoSaveAccount = Boolean.parseBoolean(new AppPreferences(getActivity().getApplicationContext()).getStringPref(AppPreferences.KEY_CHECKED_BUTTON_ACCOUNT));
        saveId.setChecked(isAutoSaveAccount);

        saveId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Test", "saveId isChecked : " + isChecked);
                saveId.setChecked(isChecked);
                new AppPreferences(getActivity().getApplicationContext()).setStringPref(AppPreferences.KEY_CHECKED_BUTTON_ACCOUNT, isChecked);
            }
        });

        autoLogin = v.findViewById(R.id.switch2);
        boolean isAutoLogin = Boolean.parseBoolean(new AppPreferences(getActivity().getApplicationContext()).getStringPref(AppPreferences.KEY_AUTO_LOGIN));
        autoLogin.setChecked(isAutoLogin);
        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("Test", "autoLogin isChecked : " + isChecked);
                autoLogin.setChecked(isChecked);
                new AppPreferences(getActivity().getApplicationContext()).setStringPref(AppPreferences.KEY_AUTO_LOGIN, isChecked);
            }
        });
//            initBottomMenu();
//        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.setting_icon_menu, menu); // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        super.onCreateOptionsMenu(menu, inflater);
    }
}
