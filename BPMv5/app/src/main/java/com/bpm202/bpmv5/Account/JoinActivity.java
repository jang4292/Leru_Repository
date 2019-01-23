package com.bpm202.bpmv5.Account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bpm202.bpmv5.BaseActivity;
import com.bpm202.bpmv5.Data.SignUpDataSource;
import com.bpm202.bpmv5.Data.SignUpRepository;
import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.Util.QToast;
import com.bpm202.bpmv5.Util.Util;

public class JoinActivity extends BaseActivity {

    private static final String TAG = JoinActivity.class.getSimpleName();

    private Button btn_duplicate;
    private Button btn_code_confirm;
    private Button btn_next;
    private EditText etEmail;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_join_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(R.string.sign_up_button_text);
        etEmail = findViewById(R.id.et_email);
        btn_duplicate = findViewById(R.id.btn_duplicate);
        btn_code_confirm = findViewById(R.id.btn_code_confirm);
        btn_next = findViewById(R.id.btn_next);

    }

    private void initListener() {
        btn_duplicate.setOnClickListener(OnClickButtonOverLapChecking);
        btn_code_confirm.setOnClickListener(OnClickButtonEmailCodeConfirmChecking);
        btn_next.setOnClickListener(OnClickButtonNextPage);
    }

    private View.OnClickListener OnClickButtonOverLapChecking = v -> {
        String email = etEmail.getText().toString().trim();

        if (Util.isValidEmail(email)) {
            AccountManager.Instance().setCurrentEmail(email);

            SignUpRepository.getInstance().checkEmail(email, new SignUpDataSource.CheckEmailCallback() {

                @Override
                public void onResponse(@NonNull String authCode, @NonNull Boolean enable) {
                    if (enable) {
                        QToast.showToast(getApplicationContext(), R.string.email_duplicate_msg);
                        AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
                    } else {
                        QToast.showToast(getApplicationContext(), R.string.email_confirm_msg);
                        AccountManager.Instance().setCheckedEmailOverLapConfirm(true);
                        AccountManager.Instance().setmEmailCode(authCode);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
                    QToast.showToast(getApplicationContext(),R.string.error_msg);
                }
            });
        } else {
            AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
            QToast.showToast(getApplicationContext(), R.string.email_is_not_valid_msg);
        }
    };
    private View.OnClickListener OnClickButtonEmailCodeConfirmChecking = v -> {

        if (v instanceof EditText) {
            String code = ((EditText) v).getText().toString().trim();

            if (AccountManager.Instance().isCorrectEmailCode(code)) {
                QToast.showToast(getApplicationContext(), R.string.email_code_confirm_msg);

            } else {
                QToast.showToast(getApplicationContext(), R.string.password_do_not_match);
            }
        }
    };

    private View.OnClickListener OnClickButtonNextPage = v -> {
        Log.d(TAG, "TEST, OnClicked");
        Log.d(TAG, "TEST, OnClicked AccountManager.Instance().toString() : " + AccountManager.Instance().toString());
    };
}
