package com.bpm202.bpmv5.Account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bpm202.bpmv5.Data.SignInRepository;
import com.bpm202.bpmv5.BaseActivity;
import com.bpm202.bpmv5.Data.SignInDataSource;
import com.bpm202.bpmv5.Exercise.MainActivity;
import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.Util.QMsg;
import com.bpm202.bpmv5.ValueObject.EmailInfoObj;
import com.bpm202.bpmv5.ValueObject.MemberObj;

public class LoginActivity extends BaseActivity {

    public static final String TAG = SignInRepository.class.getSimpleName();

    //public static final boolean IS_TEST = true;

    private Button login_btn;
    private TextView tv_find_pw;
    private EditText et_email;
    private EditText et_pw;
    private Button btn_join;

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
        tv_find_pw = findViewById(R.id.tv_find_pw);

        et_email = findViewById(R.id.et_email);
        et_pw = findViewById(R.id.et_pw);

    }

    private void initListener() {
        login_btn.setOnClickListener(OnClickEmailLogin);
        tv_find_pw.setOnClickListener(OnClickFindPassword);
        btn_join.setOnClickListener(OnClickJoinButton);
    }

    private View.OnClickListener OnClickEmailLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//            if (IS_TEST) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }

            String email = et_email.getText().toString().trim();
            String password = et_pw.getText().toString().trim();

            if (email == null || email.isEmpty()) {
                QMsg.ShowMessage(getApplicationContext(), R.string.email_input_hint);
            } else if (password == null || password.isEmpty()) {
                QMsg.ShowMessage(getApplicationContext(), R.string.password_input_hint);
            } else {
                final EmailInfoObj mEmailInfoObj = new EmailInfoObj(email, password);

                Log.d(TAG, "Before Handler");

                new Handler().postDelayed(() -> {
                    Log.d(TAG, "send Handler");
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
            Log.d(TAG, "token : " + token);
            Log.d(TAG, "member : " + memberObj.toString());

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

        @Override
        public void onDataNotAvailable() {
            Log.d(TAG, "onDataNotAvailable");
        }
    };
}
