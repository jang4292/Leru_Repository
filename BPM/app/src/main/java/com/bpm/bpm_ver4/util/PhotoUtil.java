package com.bpm.bpm_ver4.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ksh on 2018-01-04.
 * Update by ksh on 2018-08-16
 * { Update contents : deprecated modify }
 */

public class PhotoUtil {

    public final int CAMERA_REQUEST_CODE = 8;
    public final int GALLERY_REQUEST_CODE = 9;

    private String mImagePath;
    private Uri photoUri;
    private String currentPhotoPath; // 실제 사진 파일 경로
    private String mImageCaptureName; // 이미지 이름
    private Uri currentPhotoUri;

    private Activity mActivity;

    public PhotoUtil(Activity activity) {
        this.mActivity = activity;
    }


    /** 촬영해서 사진 가져오기 */
    public String selectPhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
                File photoFile = null;

                try {
                    photoFile = createImageFile();
                    currentPhotoPath = photoFile.getAbsolutePath();
                    currentPhotoUri = Uri.fromFile(photoFile);

                } catch (IOException e) {
                    e.printStackTrace();
                } // try catch

                if (photoFile != null) {
                    Context context = mActivity.getApplicationContext();
                    photoUri = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }

            } // if
        } // if

        return currentPhotoPath;
    } // selectPhoto()


    // 사진 촬영 경로 생성
    public File createImageFile() throws IOException {
        final String PATH = "/DCIM/bpm/";
        File dir = new File(Environment.getExternalStorageDirectory() + PATH);
        Log.e("path", Environment.getExternalStorageDirectory().toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(
                Environment.getExternalStorageDirectory().getAbsoluteFile()
                        + PATH + mImageCaptureName
        );

        return storageDir;
    }

    // 사진 경로
    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    // 사진 uri 경로
    public Uri getCurrentPhotoUri() {
        return currentPhotoUri;
    }


    /** 갤러리 사진 가져오기 */
    public void selectGalleyPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        mActivity.startActivityForResult(
                galleryIntent.createChooser(galleryIntent, "SelectPicture"),
                GALLERY_REQUEST_CODE
        );
    }


    // 1.사진의 절대경로와 사진의 회전값을 찾는다.
    // 2. 찾은 사진을 이미지뷰에 출력한다.
    public String sendPicture(Intent data) {
        Uri imageUri = data.getData();
        String imagePath = getRealPathFromURI(imageUri);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOreientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL
        );
        int exifDegree = exifOrientationToDegrees(exifOreientation);

        mImagePath = imagePath;
        int mImageOreantation = exifDegree;

        return mImagePath;
    }


    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(contentUri, proj,
                null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    public static String createCacheFile(Context context, byte[] bytes) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = timeStamp + ".jpg";
        File file = new File(context.getCacheDir(), fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }
}
