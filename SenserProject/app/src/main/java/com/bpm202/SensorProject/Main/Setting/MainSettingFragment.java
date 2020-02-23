package com.bpm202.SensorProject.Main.Setting;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bpm202.SensorProject.Account.LoginActivity;
import com.bpm202.SensorProject.BaseFragment;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.SplashActivity;
import com.bpm202.SensorProject.ValueObject.MemberObj;

import org.jetbrains.annotations.NotNull;

public class MainSettingFragment extends BaseFragment {


    private static MainSettingFragment instance = null;
    private TextView tvAccountData;
    private TextView tvNameData;
    private TextView tvGenderData;
    private TextView tvHeightData;
    private TextView tvWeightData;
    private TextView tvAgeData;

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

        tvAccountData = v.findViewById(R.id.tv_account_data);
        tvNameData = v.findViewById(R.id.tv_name_data);
        tvGenderData = v.findViewById(R.id.tv_gender_data);
        tvHeightData = v.findViewById(R.id.tv_height_data);
        tvWeightData = v.findViewById(R.id.tv_weight_data);
        tvAgeData = v.findViewById(R.id.tv_age_data);

        setData(LoginActivity.getMemberObject());
    }

    private void setData(@NotNull MemberObj obj) {
        String strAccount = new AppPreferences(getContext()).getStringPref(AppPreferences.KEY_SAVE_ACCOUNT);
//        Log.d("TEST", "Email : " + obj.getEmailInfo().getEmail());
        Log.d("TEST", "Email : " + strAccount);
//        Log.d("TEST", "Email : " + obj.getEMail());
        Log.d("TEST", "Name : " + obj.getInfo().getNickname());
        Log.d("TEST", "Gender : " + obj.getInfo().getGender());
        Log.d("TEST", "Height : " + obj.getInfo().getHeight());
        Log.d("TEST", "Weight : " + obj.getInfo().getWeight());
        Log.d("TEST", "Age : " + obj.getInfo().getAge());



//        tvAccountData.setText(obj.getEMail());
//        tvAccountData.setText(obj.getEmailInfo().getEmail());
        tvAccountData.setText(strAccount);
        tvNameData .setText(obj.getInfo().getNickname());
        tvGenderData .setText(obj.getInfo().getGender());
        tvHeightData .setText(String.valueOf(obj.getInfo().getHeight()));
        tvWeightData .setText(String.valueOf(obj.getInfo().getWeight()));
        tvAgeData .setText(String.valueOf(obj.getInfo().getAge()));



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.setting_icon_menu, menu); // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        super.onCreateOptionsMenu(menu, inflater);
    }
}
