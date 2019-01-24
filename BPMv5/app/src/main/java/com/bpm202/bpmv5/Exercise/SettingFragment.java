package com.bpm202.bpmv5.Exercise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bpm202.bpmv5.API.Api;
import com.bpm202.bpmv5.API.WithDrawAPI;
import com.bpm202.bpmv5.Account.LoginActivity;
import com.bpm202.bpmv5.Common.AppPreferences;
import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.SplashActivity;
import com.bpm202.bpmv5.Util.QToast;
import com.bpm202.bpmv5.ValueObject.ApiObj;

public class SettingFragment extends Fragment {

/*    private final Context mContext;

    public SettingFragment(Context context) { 
        mContext = context;
    }*/

    private static SettingFragment instance = null;

    public static SettingFragment newInstance() {
        if(instance == null) {
            instance = new SettingFragment();
        }
        return instance;
    }

    private Button btn_password;
    private Button btn_my_infomation;
    private Button btn_rule;
    private Button btn_log_out;
    private Button btn_withdraw;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.activity_setting, container, false); // 여기서 UI를 생성해서 View를 return
        initView(v);
        initListener();
        return v;
    }

    private void initView(View v) {
        //setContentView(R.layout.activity_setting);

        btn_password = v.findViewById(R.id.btn_password);
        btn_my_infomation = v.findViewById(R.id.btn_my_infomation);
        btn_rule = v.findViewById(R.id.btn_rule);
        btn_log_out = v.findViewById(R.id.btn_log_out);
        btn_withdraw = v.findViewById(R.id.btn_withdraw);
    }

    private void initListener() {
        btn_password.setOnClickListener(null);
        btn_my_infomation.setOnClickListener(null);
        btn_rule.setOnClickListener(null);
        btn_log_out.setOnClickListener(onClickLogoutListener);
        btn_withdraw.setOnClickListener(onClickWithDrawListener);
    }

    private View.OnClickListener onClickLogoutListener = v -> {
        //new AppPreferences(getApplicationContext()).setStringPref(AppPreferences.KEY_TOKEN, "");
        activityFinishToSplash();
        /*Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);*/

    };

    private View.OnClickListener onClickWithDrawListener = v -> {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.popup_title_secession);
        builder.setMessage(R.string.popup_contents_secession);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String token = new AppPreferences(getContext()).getStringPref(AppPreferences.KEY_TOKEN);
            Log.d(MainActivity.TAG, "TEST TOKEN : " + token);
            //SignInApi.secession(token,
            WithDrawAPI.secession(token,
                    callback -> {
                        ApiObj apiObj = (ApiObj) callback;
                        if (apiObj.status.equals(Api.STATUS_OK)) {
                            QToast.showToast(getContext(), R.string.menu_secession);
                            //showToast(R.string.menu_secession);
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
    }
}
