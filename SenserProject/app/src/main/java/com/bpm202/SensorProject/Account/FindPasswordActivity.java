package com.bpm202.SensorProject.Account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.API.FindPasswordAPI;
import com.bpm202.SensorProject.BaseActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.ValueObject.ApiObj;

public class FindPasswordActivity extends BaseActivity {

    public static final String TAG = FindPasswordActivity.class.getSimpleName();

    private long mLastClickTime = 0;
    private long CLICK_DELAY = 300;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(R.string.password_find_button_text);

        findViewById(R.id.btn_find).setOnClickListener(View -> {

            if (mLastClickTime < System.currentTimeMillis() - CLICK_DELAY) {
                mLastClickTime = System.currentTimeMillis();

                EditText et_email = findViewById(R.id.et_email);
                String email = et_email.getText().toString().trim();

                FindPasswordAPI.findPassword(email, callback -> {
                    Log.d(TAG, "Test, inside callBack");
                    ApiObj apiObj = (ApiObj) callback;
                    if (apiObj.status.equals(Api.STATUS_OK)) {
                        Boolean bool = (Boolean) apiObj.obj;
                        if (bool) {
                            QToast.showToast(getApplicationContext(), R.string.post_password_mail_msg);
                            finish();
                        }
                    } else {
                        QToast.showToast(getApplicationContext(), apiObj.message);
                    }
                });

            }
        });
    }


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

}
