package com.bpm.bpm_ver4.auth.region;

import com.bpm.bpm_ver4.BasePresenter;
import com.bpm.bpm_ver4.BaseView;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;

public interface RegionContract {

    interface Presenter extends BasePresenter {
        void setAdapterModel(RegionAdapterContract.Model adapterModel);
        void setAdapterView(RegionAdapterContract.View adapterView);
        void importRegionList(); // 지역리스트 가져오기
        void backPress(); // 뒤로가기 눌렀을때 액션
    }

    interface View extends BaseView<Presenter> {
        void regionListUpdateView(ArrayList<RegionObj> regionObjs); // 지역선택 뷰(리사이클러뷰) 업데이트
        void regionSelectedToReturn(RegionObj regionObj);
        void showMessage(int message);
        void showMessage(String message);
        void backPress(); // 뒤로가기
    }
}
