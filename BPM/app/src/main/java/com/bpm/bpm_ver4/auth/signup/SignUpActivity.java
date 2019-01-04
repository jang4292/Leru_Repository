package com.bpm.bpm_ver4.auth.signup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.ImageCropperActivity;
import com.bpm.bpm_ver4.exercise.MainActivity;
import com.bpm.bpm_ver4.auth.region.RegionActivity;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.databinding.ActivitySignUpBinding;
import com.bpm.bpm_ver4.testpack.TestDefined;
import com.bpm.bpm_ver4.util.LoadingUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class SignUpActivity extends BaseActivity implements SignUpContract.View {

    private ActivitySignUpBinding binding;
    private SignUpContract.Presenter mPresenter;
    private SignUpPresenter mSignUpPresenter;
    private LoadingUtil mLoadingUtil;

    private final int REQUEST_IMAGE = 8;
    private final int REQUEST_REGION = 9;

    public void test() {
        if (TestDefined.singUpActivity) {
            binding.etNick.setText("테st2019");
            binding.etHeight.setText("182");
            binding.etWeight.setText("76");
            binding.etAge.setText("20");
//            binding.btnNickCheck.callOnClick();
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        Intent intent = getIntent();

        mLoadingUtil = new LoadingUtil(this);
        mSignUpPresenter = new SignUpPresenter(this);
        mPresenter.setId(
                intent.getBooleanExtra(Common.IS_EMAIL_SIGN_UP, true),
                intent.getStringExtra(Common.ID),
                intent.getStringExtra(Common.PW)
        );

        binding.topLayer.tvTitleToolbar.setText(R.string.title_sign_up);
        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 프로필 이미지 click
        binding.ivProfileImage.setOnClickListener(
                View -> selectPhotoDialog()
        );

        // 닉네임 중복 검사 click
        binding.btnNickCheck.setOnClickListener(
                View -> mSignUpPresenter.nickDuplCheck(
                        binding.etNick.getText().toString().trim()
                )
        );

        // 지역 검색 click
        binding.btnRegionSearch.setOnClickListener(
                View -> startActivityForResult(new Intent(this, RegionActivity.class),
                        REQUEST_REGION)
        );

        // 회원 가입 click
        binding.btnSignUp.setOnClickListener(
                View -> mSignUpPresenter.signUp(
                        binding.etHeight.getText().toString().trim(),
                        binding.etWeight.getText().toString().trim(),
                        binding.etAge.getText().toString().trim(),
                        getPersonSex()
                )
        );

        test();
    }

    private String getPersonSex() {
        if (binding.radioGroupSex.getCheckedRadioButtonId() == R.id.check_male) {
            return Api.MALE;
        } else {
            return Api.FEMALE;
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                profileImageUpdateView(data.getStringExtra(Common.PATH));
            }
        } // REQUEST_IMAGE

        if (requestCode == REQUEST_REGION) {
            if (resultCode == RESULT_OK) {
                mPresenter.setLocationId(data.getIntExtra(Common.REGION_ID, -1));
                locationUpdateView(data.getStringExtra(Common.REGION_NAME));
            }
        } // REQUEST_LOCATION

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void selectPhotoDialog() {
        final CharSequence[] items = getResources().getStringArray(R.array.photo_select_str_arr);
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (items[whichButton].equals(getString(R.string.photo_shoot))) {
                    Intent intent = new Intent(SignUpActivity.this, ImageCropperActivity.class);
                    intent.putExtra(Common.IS_GALLERY, false);
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    Intent intent = new Intent(SignUpActivity.this, ImageCropperActivity.class);
                    intent.putExtra(Common.IS_GALLERY, true);
                    startActivityForResult(intent, REQUEST_IMAGE);
                }
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    @Override
    public void profileImageUpdateView(String currentImageFilePath) {
        Glide.with(this).load(currentImageFilePath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_account_circlel)
                        .error(R.drawable.ic_account_circlel)
                        .circleCrop())
                .into(binding.ivProfileImage);

        mSignUpPresenter.setImagePath(currentImageFilePath);
    }

    @Override
    public void nickDuplCehckUpdateView(boolean isDuplicate) {
        // 지금은 특별히 UI 변경이 없음
    }

    @Override
    public void locationUpdateView(String location) {
        binding.tvRegion.setText(location);
    }

    @Override
    public void signUpUpdateView() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showMessage(int message) {
        showToast(message);
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void showLoading(boolean visible) {
        if (visible)
            mLoadingUtil.show();
        else
            mLoadingUtil.dismiss();
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

}
