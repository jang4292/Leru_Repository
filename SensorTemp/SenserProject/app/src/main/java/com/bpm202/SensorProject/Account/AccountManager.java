package com.bpm202.SensorProject.Account;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.ValueObject.PersonalInfoObj;

public class AccountManager {
    private static AccountManager instance = null;

    @NonNull
    public static AccountManager Instance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    private String mEmailCodeString = null;
    private boolean isCheckedEmailOverLapConfirm = false;
    private boolean isCheckedEmailCodeConfirm = false;

    private PersonalInfoObj mPersonalInfoObj = new PersonalInfoObj();

    @NonNull
    public PersonalInfoObj getPersonalInfoObj() {
        return mPersonalInfoObj;
    }

    @NonNull
    public void setCheckedEmailOverLapConfirm(@NonNull boolean isChecked) {
        isCheckedEmailOverLapConfirm = isChecked;
    }

    @NonNull
    public boolean isCheckedEmailOverLapConfirm() {
        return isCheckedEmailOverLapConfirm;
    }

    public boolean isCheckedAllConfirmed() {
        if (isCheckedEmailOverLapConfirm || isCheckedEmailCodeConfirm) {
            return false;
        } else {
            return true;
        }
    }


    public void setmEmailCode(String eMailCode) {
        this.mEmailCodeString = eMailCode;
    }

    public boolean isCorrectEmailCode() {
        return isCheckedEmailCodeConfirm;
    }

    public boolean isCorrectEmailCode(@NonNull String code) {
        if (code.isEmpty()) {
            isCheckedEmailCodeConfirm = false;
        } else if (mEmailCodeString.equals(code)) {
            isCheckedEmailCodeConfirm = true;
        } else {
            isCheckedEmailCodeConfirm = false;
        }
        return isCheckedEmailCodeConfirm;
    }
}
