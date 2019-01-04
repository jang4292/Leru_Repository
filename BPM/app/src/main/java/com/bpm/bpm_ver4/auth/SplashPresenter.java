package com.bpm.bpm_ver4.auth;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.data.source.SignInDataSource;
import com.bpm.bpm_ver4.data.source.SignInRepository;
import com.bpm.bpm_ver4.vo.MemberObj;

public class SplashPresenter implements SplashContract.Presenter, SignInDataSource.SignInCallback {

    private SplashContract.View view;

    public SplashPresenter(SplashContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void autoLogin(String token) {
        SignInRepository repository = SignInRepository.getInstance();
        repository.signInToken(token, this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onResponse(String token, MemberObj memberObj) {
        App.setMember(memberObj);
        App.setToken(token);
        view.loginSuccessView();
        SignInRepository.destroyInstance();
    }

    @Override
    public void onDataNotAvailable() {
        view.loginFailureView();
    }
}
