package com.bpm.bpm_ver4.auth.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.ImageCropperActivity;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.CommonUrl;
import com.bpm.bpm_ver4.auth.region.RegionActivity;
import com.bpm.bpm_ver4.databinding.ActivityAccountBinding;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AccountActivity extends BaseActivity implements AccountContract.View {

    public static final String TAG = AccountActivity.class.getSimpleName();

    private ActivityAccountBinding binding;
    private AccountPresenter mAccountPresenter;
    private AccountContract.Presenter mPresenter;

    private final int REQUEST_IMAGE = 8;
    private final int REQUEST_REGION = 9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);

        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topLayer.tvTitleToolbar.setText(R.string.title_account);

        mAccountPresenter = new AccountPresenter(this);

        // 지역 검색 click
        binding.btnRegionSearch.setOnClickListener(
                View -> startActivityForResult(new Intent(this, RegionActivity.class),
                        REQUEST_REGION)
        );

        // 회원정보 수정 click
        binding.btnModify.setOnClickListener(v -> {
            mAccountPresenter.accountModify(this,
                    binding.tvNickLabel.getText().toString().trim(),
                    binding.etHeight.getText().toString().trim(),
                    binding.etWeight.getText().toString().trim(),
                    binding.etAge.getText().toString().trim(),
                    getPersonSex()
            );
        });


        binding.ivProfileImage.setOnClickListener(v -> {
            selectPhotoDialog();
        });

    }


    private String getPersonSex() {
        if (binding.radioGroup.getCheckedRadioButtonId() == R.id.check_male) {
            return Api.MALE;
        } else {
            return Api.FEMALE;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
                mPresenter.setRegionId(data.getIntExtra(Common.REGION_ID, -1));
                regionUpdateView(data.getStringExtra(Common.REGION_NAME));
            }
        } // REQUEST_LOCATION
    }

    @Override
    public void initUpdateView(PersonalInfoObj personalInfoObj) {
        binding.tvNickLabel.setText(personalInfoObj.getNickname());
        binding.etHeight.setText(String.valueOf(personalInfoObj.getHeight()));
        binding.etWeight.setText(String.valueOf(personalInfoObj.getWeight()));
        binding.etAge.setText(String.valueOf(personalInfoObj.getAge()));

        boolean isMan = personalInfoObj.getGender().equals(Api.MALE);
        binding.checkMale.setChecked(isMan);
        binding.checkFemale.setChecked(!isMan);
        initProfileView(CommonUrl.profileImageUrl + personalInfoObj.getPhoto());
    }

    @Override
    public void initProfileView(String thumbnail) {
        Glide.with(this).load(thumbnail)
                .apply(
                        new RequestOptions()
                                .placeholder(R.drawable.ic_account_circlel)
                                .error(R.drawable.ic_account_circlel)
                                .circleCrop()
                )
                .into(binding.ivProfileImage);
    }

    @Override
    public void selectPhotoDialog() {
        final CharSequence[] items = getResources().getStringArray(R.array.photo_select_str_arr);
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (items[whichButton].equals(getString(R.string.photo_shoot))) {
                    Intent intent = new Intent(AccountActivity.this, ImageCropperActivity.class);
                    intent.putExtra(Common.IS_GALLERY, false);
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    Intent intent = new Intent(AccountActivity.this, ImageCropperActivity.class);
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
    public void profileImageUpdateView(String filePath) {
        Glide.with(this).load(filePath)
                .apply(new RequestOptions()
                                .placeholder(R.drawable.ic_account_circlel)
                                .error(R.drawable.ic_account_circlel)
                                .circleCrop())
                .into(binding.ivProfileImage);

        Log.d("currentImagePath", filePath);
        mAccountPresenter.setImagePath(filePath);
    }


    @Override
    public void regionUpdateView(String regionName) {
        binding.tvRegion.setText(regionName);
    }

    @Override
    public void accountModify() {
        finish();
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void showMessage(int message) {
        showToast(message);
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
        mPresenter = presenter;
    }

}
