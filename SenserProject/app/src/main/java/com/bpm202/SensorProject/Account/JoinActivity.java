package com.bpm202.SensorProject.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.SignUpDataSource;
import com.bpm202.SensorProject.Data.SignUpRepository;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;

public class JoinActivity extends AppCompatActivity {

    private static final String TAG = JoinActivity.class.getSimpleName();

    private Button btn_next;

    private Button btn_duplicate;
    private Button btn_code_confirm;

    private EditText etEmail;
    private EditText et_email_code;

    private EditText editPassword;
    private EditText editPasswordConfirm;
    private Toolbar toolbar;
    private AppCompatCheckBox cb_agree_service;
    private AppCompatCheckBox cb_agree_private_information;

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
        et_email_code = findViewById(R.id.et_email_code);

        btn_duplicate = findViewById(R.id.btn_duplicate);
        btn_code_confirm = findViewById(R.id.btn_code_confirm);
        btn_code_confirm.setEnabled(false);
        btn_next = findViewById(R.id.btn_next);

        cb_agree_service = findViewById(R.id.cb_agree_service);
        cb_agree_private_information = findViewById(R.id.cb_agree_private_information);

        cb_agree_service.setOnCheckedChangeListener(OnAppCompatCheckBoxAgreeServiceChangeListener);
        cb_agree_private_information.setOnCheckedChangeListener(OnAppCompatCheckBoxAgreePrivateInformationChangeListener);

        editPassword = findViewById(R.id.edit_join_password);
        editPasswordConfirm = findViewById(R.id.edit_join_password_confirm);
    }

    private boolean isCheckedAgreePrivateInforamtion = false;
    private boolean isCheckedAgreePrivateService = false;
    private AppCompatCheckBox.OnCheckedChangeListener OnAppCompatCheckBoxAgreeServiceChangeListener = (buttonView, isChecked) -> {
        //TODO to need making this function for save E-mail
        isCheckedAgreePrivateService = isChecked;
        btn_next.setEnabled(isCheckedAgreePrivateService && isCheckedAgreePrivateInforamtion);
    };
    private AppCompatCheckBox.OnCheckedChangeListener OnAppCompatCheckBoxAgreePrivateInformationChangeListener = (buttonView, isChecked) -> {
        //TODO to need making this function for save E-mail
        isCheckedAgreePrivateInforamtion = isChecked;
        btn_next.setEnabled(isCheckedAgreePrivateService && isCheckedAgreePrivateInforamtion);
    };

    private void initListener() {
        btn_duplicate.setOnClickListener(OnClickButtonOverLapChecking);
        btn_code_confirm.setOnClickListener(OnClickButtonEmailCodeConfirmChecking);
        btn_next.setOnClickListener(OnClickButtonNextPage);
    }

    private View.OnClickListener OnClickButtonOverLapChecking = v -> {
        etEmail.setEnabled(false);
        btn_code_confirm.setEnabled(true);
        btn_duplicate.setText("재 인 증");
        et_email_code.requestFocus();
        Util.LoadingProgress.show(JoinActivity.this);
        String email = etEmail.getText().toString().trim();
        if (Util.isValidEmail(email)) {
            SignUpRepository.getInstance().checkEmail(email, new SignUpDataSource.CheckEmailCallback() {
                @Override
                public void onResponse(@NonNull String authCode, @NonNull Boolean enable) {
                    Util.LoadingProgress.hide();
                    if (enable) {
                        QToast.showToast(getApplicationContext(), R.string.email_duplicate_msg);
                        AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
                        etEmail.setEnabled(true);
                        btn_duplicate.setText(R.string.button_duplicate_text);
                        etEmail.requestFocus();
                    } else {
                        QToast.showToast(getApplicationContext(), R.string.email_confirm_msg);
                        AccountManager.Instance().setCheckedEmailOverLapConfirm(true);
                        AccountManager.Instance().setmEmailCode(authCode);
                    }
                }

                @Override
                public void onDataNotAvailable() {
                    Util.LoadingProgress.hide();
                    etEmail.setEnabled(true);
                    btn_duplicate.setText(R.string.button_duplicate_text);
                    etEmail.requestFocus();
                    AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
                    QToast.showToast(getApplicationContext(), R.string.error_msg);
                }
            });
        } else {
            Util.LoadingProgress.hide();
            etEmail.setEnabled(true);
            btn_duplicate.setText(R.string.button_duplicate_text);
            etEmail.requestFocus();
            AccountManager.Instance().setCheckedEmailOverLapConfirm(false);
            QToast.showToast(getApplicationContext(), R.string.email_is_not_valid_msg);
        }
    };

    private View.OnClickListener OnClickButtonEmailCodeConfirmChecking = v -> {
        if (et_email_code != null) {
            String code = et_email_code.getText().toString().trim();
            if (AccountManager.Instance().isCorrectEmailCode(code)) {
                btn_duplicate.setText("완 료");
                btn_code_confirm.setText("완 료");
                btn_duplicate.setEnabled(false);
                et_email_code.setEnabled(false);
                btn_code_confirm.setEnabled(false);
                editPassword.requestFocus();
                QToast.showToast(getApplicationContext(), R.string.email_code_confirm_msg);
            } else {
                QToast.showToast(getApplicationContext(), R.string.password_do_not_match);
            }
        }
    };

    private View.OnClickListener OnClickButtonNextPage = v -> {
        if (AccountManager.Instance().isCheckedAllConfirmed()) {
            if (!AccountManager.Instance().isCheckedEmailOverLapConfirm()) {
                QToast.showToast(JoinActivity.this, R.string.email_duplicate_check_msg);
            } else if (!AccountManager.Instance().isCorrectEmailCode()) {
                QToast.showToast(JoinActivity.this, R.string.email_code_failed_msg);
            }
        } else {
            String pw = editPassword.getText().toString().trim();
            String pwConf = editPasswordConfirm.getText().toString().trim();

            boolean isPassword = pw.equals(pwConf);
            if (!isPassword) {
                QToast.showToast(JoinActivity.this, R.string.password_do_not_match);
                return;
            }

            if (!cb_agree_service.isChecked()) {
                QToast.showToast(JoinActivity.this, R.string.check_box_agree_service);
                return;
            }

            if (!cb_agree_private_information.isChecked()) {
                QToast.showToast(JoinActivity.this, R.string.check_box_agree_private_information);
                return;
            }

            Intent intent = new Intent(this, JoinDetailsActivity.class);
            intent.putExtra(CommonData.ID, etEmail.getText().toString().trim());
            intent.putExtra(CommonData.PW, editPassword.getText().toString().trim());
            startActivity(intent);
        }
    };
}
