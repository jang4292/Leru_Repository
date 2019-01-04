package com.bpm.bpm_ver4.auth.signup;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.ApiCallback;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.data.source.SignUpDataSource;
import com.bpm.bpm_ver4.data.source.SignUpRepository;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.EmailInfoObj;
import com.bpm.bpm_ver4.vo.FailedVo;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;
import com.bpm.bpm_ver4.vo.SnsInfoObj;

public class SignUpPresenter implements SignUpContract.Presenter{

    private SignUpContract.View view;

    private PersonalInfoObj mPersonalInfoObj;
    private EmailInfoObj mEmailInfoObj;
    private SnsInfoObj mSnsInfoObj;
    private boolean isEmailSignUp;
    private boolean isNickCheck = false;
    private String mEmail;
    private String imagePath;

    private SignUpRepository mRepository;

    public SignUpPresenter(SignUpContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
        mPersonalInfoObj = new PersonalInfoObj();
        mRepository = SignUpRepository.getInstance();
    }

    @Override
    public void setId(boolean isEmailSingUp, String value1, String value2) {
        this.isEmailSignUp = isEmailSingUp;
        if (isEmailSingUp) {
            mEmailInfoObj = new EmailInfoObj(value1, value2);
            mEmail = value1;
        } else {
            mSnsInfoObj = new SnsInfoObj(value1, value2);
        }
    }

    // 이미지 경로 저장
    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // 닉네임 중복 체크
    @Override
    public void nickDuplCheck(final String nickName) {
        mRepository.checkNickname(nickName, new SignUpDataSource.CheckNicknameCallback() {
            @Override
            public void onResponse(Boolean enable) {
                if (!enable) {
                    isNickCheck = true;
                    mPersonalInfoObj.setNickname(nickName);
                    view.showMessage(R.string.nick_confirm_msg);
                } else {
                    isNickCheck = false;
                    view.showMessage(R.string.nick_duplicate_msg);
                }
            }

            @Override
            public void onDataNotAvailable() {
                view.showMessage(R.string.error_msg);
            }
        });
    }

    // 지역 아이디 저장
    @Override
    public void setLocationId(int locationId) {
        mPersonalInfoObj.setRegion(locationId);
    }

    // 회원가입 요청
    @Override
    public void signUp(String height, String weight, String age, String sex) {
        view.showLoading(true);

        // 입력 확인
        if (!isNickCheck) {
            view.showMessage(R.string.nick_check_duplicate_msg);
            return;
        }

        if (height.isEmpty()) {
            view.showMessage(R.string.sign_up_height_hint);
            return;
        }

        if (weight.isEmpty()) {
            view.showMessage(R.string.sign_up_weight_hint);
            return;
        }

        if (mPersonalInfoObj.getRegion() <= 0) {
            view.showMessage(R.string.sign_up_location_hint);
            return;
        }

        if (age.isEmpty()) {
            view.showMessage(R.string.sign_up_age_hint);
            return;
        }

        mPersonalInfoObj.setHeight(Integer.parseInt(height));
        mPersonalInfoObj.setWeight(Integer.parseInt(weight));
        mPersonalInfoObj.setAge(Integer.parseInt(age));
        mPersonalInfoObj.setGender(sex);

        MemberObj memberObj = new MemberObj();
        memberObj.setInfo(mPersonalInfoObj);

        // 이미지가 있으면 이미지 업로드 부터 하고
        // 회원 가입 처리
        if (imagePath != null && imagePath.length() > 0) {
            mRepository.photo("", imagePath, new SignUpDataSource.PhotoCallback() {
                @Override
                public void onResponse(String path) {
                    mPersonalInfoObj.setPhoto(path);
                    signUpSend(memberObj);
                }

                @Override
                public void onDataNotAvailable() {
                    view.showMessage(R.string.error_msg);
                }
            });
        }
        // 이미지가 없으면 회원가입 처리만
        else {
            mPersonalInfoObj.setPhoto("");
            signUpSend(memberObj);
        }


    }

    @Override
    public void start() {

    }

    private void signUpSend(MemberObj memberObj) {
        if (isEmailSignUp) {
            memberObj.setEmailInfo(mEmailInfoObj);
            mRepository.signUpWithEmail(memberObj, signUpCallback);
        } else {
            memberObj.setSnsInfo(mSnsInfoObj);
            mRepository.signUpWithSns(memberObj, signUpCallback);
        }
    }



    private SignUpDataSource.SignUpCallback signUpCallback = new SignUpDataSource.SignUpCallback() {
        @Override
        public void onResponse(String token, MemberObj memberObj) {
            new AppPref(view.getContext().getApplicationContext()).setStringPref(AppPref.KEY_TOKEN, token);
            App.setToken(token);
            App.setMember(memberObj);
            App.setEmail(mEmail);
            view.signUpUpdateView();
        }

        @Override
        public void onDataNotAvailable() {
            view.showLoading(false);
            view.showMessage(R.string.error_msg);
        }
    };


}
