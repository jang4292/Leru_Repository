package com.bpm.bpm_ver4.util.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class KaKaoLoginHelper implements LoginInHelper {

    private Activity mActivity;
    private Context mContext;



    public KaKaoLoginHelper(Activity activity) {
        this.mActivity = activity;
    }

    /******************** interface  ******************************/
    @Override
    public void loginPressImp() {

    }
    /******************** interface  ******************************/
    @Override
    public void logoutPressImp() {

    }
    /******************** interface  ******************************/
    @Override
    public void secessionImp() {

    }
    /******************** interface  ******************************/
    @Override
    public void onStartImp() {

    }
    /******************** interface  ******************************/
    @Override
    public void onActivityResultImp(int requestCode, int resultCode, Intent data) {

    }
}
