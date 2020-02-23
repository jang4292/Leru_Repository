package com.bpm202.SensorProject.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
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
import com.bpm202.SensorProject.ValueObject.PersonalInfoObj;
import com.bpm202.SensorProject.db.DBUser;

import java.util.ArrayList;

public class JoinDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnCheckName;
    private Button btnSignUp;
    private EditText editName;
    private EditText etHeight;
    private EditText etWeight;
    private EditText etAge;
    private RadioGroup radioGroupSex;
    private TextView tvRegion;
    private Button btnRegion;

    private EmailInfoObj mEmailInfoObj;

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

        editName = findViewById(R.id.edit_name);
        btnCheckName = findViewById(R.id.btn_name_check);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        etAge = findViewById(R.id.et_age);
        tvRegion = findViewById(R.id.tv_region);
        btnRegion = findViewById(R.id.btn_region);
        radioGroupSex = findViewById(R.id.radio_group_sex);
        btnRegion = findViewById(R.id.btn_region);
        btnSignUp = findViewById(R.id.btn_sign_up);
    }

    private final int REQUEST_REGION = 9;

    private void initData() {
        Intent intent = getIntent();
        setId(intent.getStringExtra(CommonData.ID), intent.getStringExtra(CommonData.PW));

        btnCheckName.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            SignUpRepository.getInstance().checkNickname(name, new SignUpDataSource.CheckNicknameCallback() {
                @Override
                public void onResponse(Boolean enable) {
                    if (!enable) {
                        AccountManager.Instance().getPersonalInfoObj().setNickname(name);
                        btnCheckName.setEnabled(false);
                        btnCheckName.setText(R.string.button_confirm_text);
                        editName.setEnabled(false);
                        QToast.showToast(JoinDetailsActivity.this, R.string.nick_confirm_msg);
                    } else {
                        editName.requestFocus();
                        QToast.showToast(JoinDetailsActivity.this, R.string.nick_duplicate_msg);

                    }
                }

                @Override
                public void onDataNotAvailable() {
                    QToast.showToast(JoinDetailsActivity.this, R.string.error_msg);
                }
            });
        });

        btnRegion.setOnClickListener(v -> startActivityForResult(new Intent(JoinDetailsActivity.this, RegionActivity.class), REQUEST_REGION));
        btnSignUp.setOnClickListener(v -> signUp(editName.getText().toString().trim(), etHeight.getText().toString().trim(),
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

    private void setId(String value1, String value2) {
        mEmailInfoObj = new EmailInfoObj(value1, value2);
    }

    private String getPersonSex() {
        if (radioGroupSex.getCheckedRadioButtonId() == R.id.rg_btn_male) {
            return Api.MALE;
        } else {
            return Api.FEMALE;
        }
    }


    public void signUp(String name, String height, String weight, String age, String sex) {
        // 입력 확인
        if (name == null || name.isEmpty()) {
            QToast.showToast(JoinDetailsActivity.this, R.string.nick_check_duplicate_msg);
            return;
        }

        if (height == null || height.isEmpty()) {
            QToast.showToast(JoinDetailsActivity.this, R.string.sign_up_height_hint);
            return;
        }

        if (weight == null || weight.isEmpty()) {
            QToast.showToast(JoinDetailsActivity.this, R.string.sign_up_weight_hint);
            return;
        }

        if (age == null || age.isEmpty()) {
            QToast.showToast(JoinDetailsActivity.this, R.string.sign_up_age_hint);
            return;
        }

        if (AccountManager.Instance().getPersonalInfoObj().getRegion() <= 0) {
            QToast.showToast(JoinDetailsActivity.this, R.string.sign_up_location_hint);
            return;
        }

        AccountManager.Instance().getPersonalInfoObj().setNickname(name);
        AccountManager.Instance().getPersonalInfoObj().setHeight(Integer.parseInt(height));
        AccountManager.Instance().getPersonalInfoObj().setWeight(Integer.parseInt(weight));
        AccountManager.Instance().getPersonalInfoObj().setAge(Integer.parseInt(age));
        AccountManager.Instance().getPersonalInfoObj().setGender(sex);
        AccountManager.Instance().getPersonalInfoObj().setPhoto("");
        AccountManager.Instance().getPersonalInfoObj().setRegionStr(tvRegion.getText().toString().trim());

        MemberObj memberObj = new MemberObj();
        memberObj.setInfo(AccountManager.Instance().getPersonalInfoObj());

        memberObj.setEmailInfo(mEmailInfoObj);
//        Util.LoadingProgress.show(JoinDetailsActivity.this);
        SignUpRepository.getInstance().signUpWithEmail(memberObj, new SignUpDataSource.SignUpCallback() {
            @Override
            public void onResponse(String token, MemberObj memberObj) {
                saveUserDB();

                new AppPreferences(JoinDetailsActivity.this).setStringPref(AppPreferences.KEY_TOKEN, token);
                App.setToken(token);
                Util.LoadingProgress.hide();
                Intent intent = new Intent(JoinDetailsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onDataNotAvailable() {
                Util.LoadingProgress.hide();
                QToast.showToast(JoinDetailsActivity.this, R.string.error_msg);
            }
        });
    }

    private void saveUserDB() {
        PersonalInfoObj obj = AccountManager.Instance().getPersonalInfoObj();
        obj.setEmail(mEmailInfoObj.getEmail());
        DBUser db = new DBUser(getApplicationContext());
        db.open();
        ArrayList<PersonalInfoObj> lists = db.selectDB(obj);
        if (lists.isEmpty()) {
            db.insertDB(obj);
        }
        db.close();
    }
}
