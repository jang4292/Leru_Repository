package com.bpm.bpm_ver4.auth.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.testpack.TestDefined;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.EmailInfoObj;

public class ChangePasswordActivity extends BaseActivity {

    private long mLastClickTime = 0;
    private long CLICK_DELAY = 300;
    private String email;

    private void test() {
        if (TestDefined.changePasswordActivity) {
//            ((EditText) findViewById(R.id.et_pw)).setText("");
//            ((EditText) findViewById(R.id.et_pw_confirm)).setText("");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        test();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvToolbarTitle = findViewById(R.id.tv_title_toolbar);
        tvToolbarTitle.setText(getString(R.string.title_change_pw));

        email = App.getEmail();
        ((EditText) findViewById(R.id.et_email)).setText(email);

        findViewById(R.id.btn_change_pw).setOnClickListener(v -> {
            if (mLastClickTime < (System.currentTimeMillis() - CLICK_DELAY)) {
                mLastClickTime = System.currentTimeMillis();

                EditText et_pw = findViewById(R.id.et_pw);
                EditText et_pw_confirm = findViewById(R.id.et_pw_confirm);

                String pw = et_pw.getText().toString().trim();
                String pw_confirm = et_pw_confirm.getText().toString().trim();

                // 비밀번호를 입력하지 않았을 경우 토스트 출력
                if (pw.isEmpty() || pw_confirm.isEmpty()) {
                    showToast(R.string.please_enter_your_password_msg);
                    return;
                }

                // 비밀번호가 일치 하지 않았을 경우 토스트 출력
                if (!pw.equals(pw_confirm)) {
                    showToast(R.string.password_do_not_match);
                    return;
                }

                String token = getToken();
                EmailInfoObj emailInfoObj = new EmailInfoObj();
                emailInfoObj.setEmail(email);
                emailInfoObj.setPassword(pw);

                SignInApi.changePassword(token, emailInfoObj, callback -> {
                    ApiObj apiObj = (ApiObj) callback;
                    if (apiObj.status.equals(Api.STATUS_OK)) {
                        Boolean bool = (Boolean) apiObj.obj;
                        if (bool) {
                            showToast(R.string.change_password_msg);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}









