package com.bpm.bpm_ver4.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.databinding.ActivityImageCropperBinding;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class ImageCropperActivity extends BaseActivity implements CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener {

    private ActivityImageCropperBinding binding;
    private PhotoUtil mPhotoUtil;

    private boolean isSelect = false;

    private int REQUEST_EXTERNAL_PERMISSION = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_cropper);
        permission();

        mPhotoUtil = new PhotoUtil(this);

        binding.cropImageView.setAspectRatio(1, 1);

        binding.topLayer.tvTitleToolbar.setText(R.string.title_select_photo);
        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.cropImageView.setOnSetImageUriCompleteListener(this);
        binding.cropImageView.setOnCropImageCompleteListener(this);

//        // 사진 선택
//        binding.btnSelect.setOnClickListener(
//                View -> {
//                    if (isSelect) {
//                        binding.cropImageView.getCroppedImageAsync();
//                    }
//                });
//
//        // 사진 선택 취소
//        binding.btnCancel.setOnClickListener(
//                View -> onBackPressed()
//        );


        if (getIntent().getBooleanExtra(Common.IS_GALLERY, true)) {
            mPhotoUtil.selectGalleyPhoto();
        } else {
            mPhotoUtil.selectPhoto();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_cropper_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;

            case R.id.action_select :
                binding.cropImageView.getCroppedImageAsync();
                break;

//            case R.id.action_photo :
//                mPhotoUtil.selectPhoto();
//                break;
//
//            case R.id.action_gallery :
//                mPhotoUtil.selectGalleyPhoto();
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == mPhotoUtil.GALLERY_REQUEST_CODE) {
                binding.cropImageView.setImageUriAsync(data.getData());
                isSelect = true;
            } // GALLERY_REQUEST_CODE

            if (requestCode == mPhotoUtil.CAMERA_REQUEST_CODE) {
                binding.cropImageView.setImageUriAsync(mPhotoUtil.getCurrentPhotoUri());
                isSelect = true;
            }


            if (requestCode == REQUEST_EXTERNAL_PERMISSION) {

            }

        } // RESULT_OK

    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(this, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }


    private void handleCropResult(CropImageView.CropResult result) {
        Bitmap bitmap = result.getBitmap();
        result(bitmap);
//        new CreateImageFile(bitmap).execute();
    }


    private void result(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String filePath = PhotoUtil.createCacheFile(this, byteArray);

        Intent intent = new Intent();
        intent.putExtra(Common.PATH, filePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateUI(Bitmap bitmap) {
        binding.cropImageView.clearImage();
        binding.cropImageView.setImageBitmap(bitmap);
    }



    private void permission() {
        // 파일 읽기/쓰기 권한
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_PERMISSION);
        }
    }
}
