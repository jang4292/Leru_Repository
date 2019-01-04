package com.bpm.bpm_ver4.exercise;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;

public interface MainContract {

    interface Presenter extends BasePresenter {


    }


    interface View extends BaseView<Presenter> {

    }


}
