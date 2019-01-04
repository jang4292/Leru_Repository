package com.bpm.bpm_ver4.auth.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.testpack.TestDefined;
import com.bpm.bpm_ver4.vo.ApiObj;


public class FindPasswordActivity extends BaseActivity {

    private long mLastClickTime = 0;
    private long CLICK_DELAY = 300;

    private void test() {
        if (TestDefined.findPasswordActivity) {
            ((EditText) findViewById(R.id.et_email)).setText("202bpm.com@gmail.com");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        test();

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.tv_title_toolbar);
        title.setText(R.string.title_find_pw);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_find).setOnClickListener(View -> {
            if (mLastClickTime < (System.currentTimeMillis() - CLICK_DELAY)) {
                mLastClickTime = System.currentTimeMillis();

                EditText et_email = findViewById(R.id.et_email);
                String email = et_email.getText().toString().trim();

                SignInApi.findPassword(email, callback -> {
                    ApiObj apiObj = (ApiObj) callback;
                    if (apiObj.status.equals(Api.STATUS_OK)) {
                        Boolean bool = (Boolean) apiObj.obj;
                        if (bool) {
                            showToast(R.string.post_password_mail_msg);
                            finish();
                        }
                    } else {
                        showToast(apiObj.message);
                    }
                });

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
