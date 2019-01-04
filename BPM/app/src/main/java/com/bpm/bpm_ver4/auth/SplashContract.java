package com.bpm.bpm_ver4.auth;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;

public interface SplashContract {

    interface Presenter extends BasePresenter {
        void autoLogin(String token);
    }

    interface View extends BaseView<Presenter> {
        void loginSuccessView();
        void loginFailureView();
    }
}
