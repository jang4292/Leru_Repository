package com.bpm202.bpmv5.Account;


import android.content.Context;
import android.support.annotation.NonNull;

import com.bpm202.bpmv5.R;
import com.bpm202.bpmv5.Util.QToast;

public class AccountManager {

//    public class CheckedErroCode {
//
//    }

    private static AccountManager instance = null;

    //public static AccountManager Instance(Context context) {
    @NonNull
    public static AccountManager Instance() {

        if (instance == null) {
            //instance = new AccountManager(context);
            instance = new AccountManager();
        }
        return instance;
    }

    //private Context mContext;

    /*public AccountManager(Context context) {
        this.mContext = context;
    }*/

    private String mEmailString = null;



    private String mEmailCodeString = null;
    private boolean isCheckedEmailOverLapConfirm = false;
    private boolean isCheckedEmailCodeConfirm = false;
    private boolean isCheckedPasswordConfirm = false;

    @NonNull
    public String getCurrentEmail() {
        return mEmailString;
    }

    @NonNull
    public void setCurrentEmail(@NonNull String string) {
        mEmailString = string;
    }


    @NonNull
    public void setCheckedEmailOverLapConfirm(@NonNull boolean isChecked) {
        isCheckedEmailOverLapConfirm = isChecked;
    }

/*    @NonNull
    public void setCheckedEmailCodeConfirm(@NonNull boolean checkedEmailCodeConfirm) {
        isCheckedEmailCodeConfirm = checkedEmailCodeConfirm;
    }*/

    @NonNull
    public void setCheckedPasswordConfirm(@NonNull boolean checkedPasswordConfirm) {
        isCheckedPasswordConfirm = checkedPasswordConfirm;
    }

    public boolean isCheckedAllConfirmed() {
        if (isCheckedEmailOverLapConfirm || isCheckedEmailCodeConfirm || isCheckedPasswordConfirm) {
            /*if (!isCheckedEmailOverLapConfirm) {
                //showMessage(R.string.email_duplicate_check_msg);
                QToast.showToast(mContext, R.string.email_duplicate_check_msg);
                return false;
            }
            if (!isCheckedEmailCodeConfirm) {
                //showMessage(R.string.email_code_failed_msg);
                QToast.showToast(mContext, R.string.email_code_failed_msg);
                return false;
            }
            if (!isCheckedPasswordConfirm) {
                //showMessage(R.string.password_do_not_match);
                QToast.showToast(mContext, R.string.password_do_not_match);
                return false;
            }*/
            return false;
        } else {
            return true;
        }
    }


    public void setmEmailCode(String eMailCode) {
        this.mEmailCodeString = eMailCode;
    }

/*    public String getmEmailCode() {
        return mEmailCodeString;
    }*/

    public boolean isCorrectEmailCode(@NonNull String code) {

        if (code.isEmpty()) {

            isCheckedEmailCodeConfirm = false;

            //manager.setCheckedEmailCodeConfirm(false);
            //QToast.showToast(getApplicationContext(), R.string.email_code_failed_msg);
        } else if (mEmailCodeString.equals(code)) {
            isCheckedEmailCodeConfirm = true;
            //manager.setCheckedEmailCodeConfirm(true);
            //QToast.showToast(getApplicationContext(), R.string.email_code_confirm_msg);
        } else {
            isCheckedEmailCodeConfirm = false;
            //manager.setCheckedEmailCodeConfirm(false);
            //QToast.showToast(getApplicationContext(), R.string.email_code_failed_msg);
        }
        return isCheckedEmailCodeConfirm;


        //return false;
    }

/*    @NonNull
    public boolean isCheckedEmailOverLapConfirm() {
        return isCheckedEmailOverLapConfirm;
    }*/


}
