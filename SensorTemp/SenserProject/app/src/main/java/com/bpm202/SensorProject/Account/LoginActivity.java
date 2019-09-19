package com.bpm202.SensorProject.Account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bpm202.SensorProject.App;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Data.SignInDataSource;
import com.bpm202.SensorProject.Data.SignInRepository;
import com.bpm202.SensorProject.Main.Exercise.ExerciseActivity;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.EmailInfoObj;
import com.bpm202.SensorProject.ValueObject.MemberObj;

public class LoginActivity extends Activity {

    public static final String TAG = SignInRepository.class.getSimpleName();

    private Button login_btn;
    //private TextView tv_find_pw;
    private Button tv_find_pw;
    private EditText et_email;
    private EditText editLoginPassword;
    private Button btn_join;
    private AppCompatCheckBox cb_save;
    private AppCompatCheckBox cb_auto_login;

    private boolean isCheckedAutoLogin;
    private String _eMail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);
        btn_join = findViewById(R.id.btn_join);
        //tv_find_pw = findViewById(R.id.tv_find_pw);

        tv_find_pw = findViewById(R.id.btn_find_pw);


        et_email = findViewById(R.id.et_email);
        editLoginPassword = findViewById(R.id.edit_login_password);

        cb_save = findViewById(R.id.cb_save);
        cb_auto_login = findViewById(R.id.cb_auto_login);

        boolean isAutoLogin = Boolean.parseBoolean(new AppPreferences(getApplicationContext()).getStringPref(AppPreferences.KEY_AUTO_LOGIN));
        cb_auto_login.setChecked(isAutoLogin);
        boolean isAutoSaveAccount = Boolean.parseBoolean(new AppPreferences(getApplicationContext()).getStringPref(AppPreferences.KEY_CHECKED_BUTTON_ACCOUNT));
        cb_save.setChecked(isAutoSaveAccount);
        if (isAutoSaveAccount) {
            String strAccount = new AppPreferences(getApplicationContext()).getStringPref(AppPreferences.KEY_SAVE_ACCOUNT);
            et_email.setText(strAccount);
        }
    }

    private void initListener() {
        login_btn.setOnClickListener(OnClickEmailLogin);
        tv_find_pw.setOnClickListener(OnClickFindPassword);
        btn_join.setOnClickListener(OnClickJoinButton);

        cb_save.setOnCheckedChangeListener(OnAppCompatCheckBoxSaveChangeListener);
        cb_auto_login.setOnCheckedChangeListener(OnAppCompatCheckBoxAutoLoginChangeListener);
    }

    private boolean isCheckedSaveAccount = false;
    private AppCompatCheckBox.OnCheckedChangeListener OnAppCompatCheckBoxSaveChangeListener = (buttonView, isChecked) -> {
        //TODO to need making this function for save E-mail
        this.isCheckedSaveAccount = isChecked;
    };


    private AppCompatCheckBox.OnCheckedChangeListener OnAppCompatCheckBoxAutoLoginChangeListener = (buttonView, isChecked) -> {
        //TODO to need making this function of Auto Login
        this.isCheckedAutoLogin = isChecked;
    };

    private View.OnClickListener OnClickEmailLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            /*if (true) {
                Intent intent = new Intent(LoginActivity.this, MainActivity_sub.class);
                startActivity(intent);
                finish();
                return;
            }*/

            String email = et_email.getText().toString().trim();
            String password = editLoginPassword.getText().toString().trim();

            if (email == null || email.isEmpty()) {
                QToast.showToast(getApplicationContext(), R.string.email_input_hint);
            } else if (password == null || password.isEmpty()) {
                QToast.showToast(getApplicationContext(), R.string.password_input_hint);
            } else {


                _eMail = email;
                final EmailInfoObj mEmailInfoObj = new EmailInfoObj(email, password);

                Util.LoadingProgress.show(LoginActivity.this);
                new Handler().postDelayed(() -> {

                    SignInRepository.getInstance().signInWithEmail(mEmailInfoObj, callback);
                }, 500);
            }
        }
    };

    private View.OnClickListener OnClickFindPassword = v -> {
        Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener OnClickJoinButton = v -> {
        Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
        startActivity(intent);
    };

    private SignInDataSource.SignInCallback callback = new SignInDataSource.SignInCallback() {

        @Override
        public void onResponse(String token, MemberObj memberObj) {
            Log.i(TAG, "token : " + token);
            Log.i(TAG, "member : " + memberObj.toString());
            Log.i(TAG, "auto Login : " + isCheckedAutoLogin);

            new AppPreferences(getApplicationContext()).setStringPref(AppPreferences.KEY_AUTO_LOGIN, isCheckedAutoLogin);
            new AppPreferences(getApplicationContext()).setStringPref(AppPreferences.KEY_TOKEN, token);
            new AppPreferences(getApplicationContext()).setStringPref(AppPreferences.KEY_CHECKED_BUTTON_ACCOUNT, isCheckedSaveAccount);
            new AppPreferences(getApplicationContext()).setStringPref(AppPreferences.KEY_SAVE_ACCOUNT, _eMail);

            App.setToken(token);

//            Intent intent = new Intent(LoginActivity.this, MainActivity_sub.class);
//            Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
//            Intent intent = new Intent(LoginActivity.this, WeeksPlanActivity.class);
//            Intent intent = new Intent(LoginActivity.this, ExerciseActivity.class);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            Util.LoadingProgress.hide();
            finish();
        }

        @Override
        public void onDataNotAvailable() {
            Log.e(TAG, "onDataNotAvailable");
            Util.LoadingProgress.hide();
        }
    };
}
