package com.bpm202.SensorProject.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bpm202.SensorProject.API.Api;
import com.bpm202.SensorProject.App;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.SignUpDataSource;
import com.bpm202.SensorProject.Data.SignUpRepository;
import com.bpm202.SensorProject.Main.MainActivity;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.EmailInfoObj;
import com.bpm202.SensorProject.ValueObject.MemberObj;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton ivProfileImage;
    private Button btnNickCheck;
    private Button btnRegionSearch;
    private Button btnSignUp;
    private EditText etNick;
    private boolean isNickCheck;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etAge;
    private RadioGroup radioGroupSex;
    private TextView tvRegion;

    private boolean isEmailSignUp;
    private EmailInfoObj mEmailInfoObj;
    private String mEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }


    private void initView() {
        setContentView(R.layout.activity_join_sub);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(R.string.sign_up_button_text);

        etNick = findViewById(R.id.et_nick);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        etAge = findViewById(R.id.et_age);
        tvRegion = findViewById(R.id.tv_region);

        radioGroupSex = findViewById(R.id.radio_group_sex);


        ivProfileImage = findViewById(R.id.iv_profile_image);
        btnNickCheck = findViewById(R.id.btn_nick_check);
        btnRegionSearch = findViewById(R.id.btn_region_search);
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    private final int REQUEST_REGION = 9;

    private void initData() {
        Intent intent = getIntent();
        setId(intent.getBooleanExtra(CommonData.IS_EMAIL_SIGN_UP, true), intent.getStringExtra(CommonData.ID), intent.getStringExtra(CommonData.PW));

        ivProfileImage.setOnClickListener(v -> QToast.showToast(SignUpActivity.this, "Wait and delay"));

        btnNickCheck.setOnClickListener(v -> {
            String name = etNick.getText().toString().trim();
            SignUpRepository.getInstance().checkNickname(name, new SignUpDataSource.CheckNicknameCallback() {
                @Override
                public void onResponse(Boolean enable) {
                    if (!enable) {
                        isNickCheck = true;
                        AccountManager.Instance().getPersonalInfoObj().setNickname(name);
                        QToast.showToast(SignUpActivity.this, R.string.nick_confirm_msg);
                    } else {
                        isNickCheck = false;
                        QToast.showToast(SignUpActivity.this, R.string.nick_duplicate_msg);

                    }
                }

                @Override
                public void onDataNotAvailable() {
                    QToast.showToast(SignUpActivity.this, R.string.error_msg);
                }
            });
        });

        btnRegionSearch.setOnClickListener(v -> startActivityForResult(new Intent(SignUpActivity.this, RegionActivity.class), REQUEST_REGION));

        btnSignUp.setOnClickListener(v -> signUp(etHeight.getText().toString().trim(),
                etWeight.getText().toString().trim(),
                etAge.getText().toString().trim(),
                getPersonSex()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGION) { // REQUEST_IMAGE
            if (resultCode == RESULT_OK) {
                AccountManager.Instance().getPersonalInfoObj().setRegion(data.getIntExtra(CommonData.REGION_ID, -1));
                tvRegion.setText(data.getStringExtra(CommonData.REGION_NAME));
            }
        } // REQUEST_LOCATION

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setId(boolean isEmailSingUp, String value1, String value2) {
        isEmailSignUp = isEmailSingUp;
        if (isEmailSingUp) {
            mEmailInfoObj = new EmailInfoObj(value1, value2);
            mEmail = value1;
        }
    }

    private String getPersonSex() {
        if (radioGroupSex.getCheckedRadioButtonId() == R.id.check_male) {
            return Api.MALE;
        } else {
            return Api.FEMALE;
        }
    }


    public void signUp(String height, String weight, String age, String sex) {
        // 입력 확인
        if (!isNickCheck) {
            QToast.showToast(SignUpActivity.this, R.string.nick_check_duplicate_msg);
            return;
        }

        if (height.isEmpty()) {
            QToast.showToast(SignUpActivity.this, R.string.sign_up_height_hint);
            return;
        }

        if (weight.isEmpty()) {
            QToast.showToast(SignUpActivity.this, R.string.sign_up_weight_hint);
            return;
        }

        if (AccountManager.Instance().getPersonalInfoObj().getRegion() <= 0) {
            QToast.showToast(SignUpActivity.this, R.string.sign_up_location_hint);
            return;
        }

        if (age.isEmpty()) {
            QToast.showToast(SignUpActivity.this, R.string.sign_up_age_hint);
            return;
        }

        AccountManager.Instance().getPersonalInfoObj().setHeight(Integer.parseInt(height));
        AccountManager.Instance().getPersonalInfoObj().setWeight(Integer.parseInt(weight));
        AccountManager.Instance().getPersonalInfoObj().setAge(Integer.parseInt(age));
        AccountManager.Instance().getPersonalInfoObj().setGender(sex);
        AccountManager.Instance().getPersonalInfoObj().setPhoto("");

        MemberObj memberObj = new MemberObj();
        memberObj.setInfo(AccountManager.Instance().getPersonalInfoObj());

        if (isEmailSignUp) {
            memberObj.setEmailInfo(mEmailInfoObj);
            Util.LoadingProgress.show(SignUpActivity.this);
            SignUpRepository.getInstance().signUpWithEmail(memberObj, new SignUpDataSource.SignUpCallback() {
                @Override
                public void onResponse(String token, MemberObj memberObj) {
                    new AppPreferences(SignUpActivity.this).setStringPref(AppPreferences.KEY_TOKEN, token);
                    App.setToken(token);
                    Util.LoadingProgress.hide();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                @Override
                public void onDataNotAvailable() {
                    Util.LoadingProgress.hide();
                    QToast.showToast(SignUpActivity.this, R.string.error_msg);
                }
            });
        }
    }
}
