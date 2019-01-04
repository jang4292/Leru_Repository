package com.bpm.bpm_ver4.auth.account;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.ApiCallback;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.data.source.SignUpDataSource;
import com.bpm.bpm_ver4.data.source.SignUpRepository;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class AccountPresenter implements AccountContract.Presenter {

    public static final String TAG = AccountPresenter.class.getSimpleName();

    private AccountContract.View view;

    private MemberObj memberObj;
    private PersonalInfoObj personalInfoObj;
    private String imagePath;
    private int regionId;

    public AccountPresenter(AccountContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        initData();
    }

    @Override
    public void initData() {
        memberObj = App.getMember();
        personalInfoObj = memberObj.getInfo();
        setRegionId(personalInfoObj.getRegion());
        view.initUpdateView(personalInfoObj);
        checkRegion();
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    @Override
    public void accountModify(Context context, String nick, String height, String weight, String age, String gender) {
        if (nick.isEmpty()) {
            view.showMessage(R.string.sign_up_nick_hint);
            return;
        }

        if (height.isEmpty()) {
            view.showMessage(R.string.sign_up_height_hint);
            return;
        }

        if (weight.isEmpty()){
            view.showMessage(R.string.sign_up_weight_hint);
            return;
        }

        if (age.isEmpty()) {
            view.showMessage(R.string.sign_up_age_hint);
            return;
        }

        if (gender.isEmpty()) {
            view.showMessage(R.string.sign_up_gender_hint);
            return;
        }

        PersonalInfoObj infoObj = new PersonalInfoObj();
        infoObj.setNickname(nick);
        infoObj.setHeight(Integer.parseInt(height));
        infoObj.setWeight(Integer.parseInt(weight));
        infoObj.setAge(Integer.parseInt(age));
        infoObj.setGender(gender);
        infoObj.setRegion(regionId);

        if (imagePath != null && imagePath.length() > 0) {
            profileUpload(context, infoObj);
        } else {
            accountModifySend(context, infoObj, "");
        }
    }

    private void profileUpload(Context context, PersonalInfoObj infoObj) {
        SignUpRepository repository = SignUpRepository.getInstance();
        repository.photo(personalInfoObj.getPhoto(), imagePath, new SignUpDataSource.PhotoCallback() {
            @Override
            public void onResponse(String path) {
                accountModifySend(context, infoObj, path);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

    }

    private void accountModifySend(Context context, PersonalInfoObj infoObj, String newThumbnail) {
        if (newThumbnail != null && newThumbnail.length() > 0) {
            infoObj.setPhoto(newThumbnail);
        } else {
            infoObj.setPhoto(personalInfoObj.getPhoto());
        }


        String token = new AppPref(context).getStringPref(AppPref.KEY_TOKEN);
        SignInApi.modifyAccount(token, infoObj,
                callback -> {
                    ApiObj apiObj = (ApiObj) callback;
                    if (apiObj.status.equals(Api.STATUS_OK)) {
                        personalInfoObj = (PersonalInfoObj) apiObj.obj;
                        memberObj.setInfo(personalInfoObj);
                        App.setMember(memberObj);
                        view.showMessage(R.string.modify_msg);
                        view.accountModify();
                    } else {
                        Toast.makeText(context, apiObj.message, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, apiObj.message);
                    }
                });
    }


    private void findRegion(List<RegionObj> objs) {
        for (RegionObj obj : objs) {
            if (regionId == obj.id) {
                view.regionUpdateView(String.format("%s %s", obj.main, obj.sub));
                break;
            }
        }

    }

    private void checkRegion() {
        SignUpRepository repository = SignUpRepository.getInstance();
        repository.region(new SignUpDataSource.RegionCallback() {
            @Override
            public void onResponse(List<RegionObj> regionObjList) {
                findRegion(regionObjList);
            }

            @Override
            public void onDataNotAvailable() {
                view.showMessage(R.string.error_msg);
            }
        });

    }

    @Override
    public void start() {

    }
}
