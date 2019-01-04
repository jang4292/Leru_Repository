package com.bpm.bpm_ver4.auth.region;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.source.SignUpDataSource;
import com.bpm.bpm_ver4.data.source.SignUpRepository;
import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.ApiCallback;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class RegionPresenter implements RegionContract.Presenter, OnItemClickListener {

    private RegionContract.View view;
    private List<RegionObj> mRegionObjs;

    private RegionAdapterContract.Model mAdapterModel;
    private RegionAdapterContract.View mAdapterView;

    public RegionPresenter(RegionContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onItemClick(int position) {
        if (mAdapterModel.isProvince()) {
            String province = mAdapterModel.getProince(position);
            mAdapterModel.addItems(subRegion(province));
            mAdapterModel.isProvince(false);
            mAdapterView.notifyAdapter();
        } else {
            view.regionSelectedToReturn(mAdapterModel.getItem(position));
        }
    }

    @Override
    public void setAdapterModel(RegionAdapterContract.Model adapterModel) {
        this.mAdapterModel = adapterModel;
    }

    @Override
    public void setAdapterView(RegionAdapterContract.View adapterView) {
        this.mAdapterView = adapterView;
        this.mAdapterView.setOnItemClickListener(this);
    }

    @Override
    public void importRegionList() {
//        if (FakeData.TEST) {
//            mRegionObjs = FakeData.getFakeRegionData();
//            mAdapter.setProvince(mainRegion(mRegionObjs));
//            mAdapter.notifyDataSetChanged();
//        }

        SignUpRepository repository = SignUpRepository.getInstance();
        repository.region(new SignUpDataSource.RegionCallback() {
            @Override
            public void onResponse(List<RegionObj> regionObjList) {
                importRegion(regionObjList);
            }

            @Override
            public void onDataNotAvailable() {
                view.showMessage(R.string.error_msg);
            }
        });

    }

    @Override
    public void backPress() {
        if (!mAdapterModel.isProvince()) {
            mAdapterModel.isProvince(true);
            mAdapterView.notifyAdapter();
        } else {
            view.backPress();
        }
    }


    @Override
    public void start() {

    }


    private void importRegion(List<RegionObj> regionObjs) {
        mRegionObjs = regionObjs;
        mAdapterModel.addProvince(mainRegion(mRegionObjs));
        mAdapterView.notifyAdapter();
    }


    // 도 지역 추출(또는 광역시 이상의 시)
    private List<String> mainRegion(List<RegionObj> regionObjs) {
        List<String> provinceName = new ArrayList<>();
        String temp = "";
        for (RegionObj obj : regionObjs) {
            if (!temp.equals(obj.main)) {
                temp = obj.main;
                provinceName.add(temp);
            }
        }

        return provinceName;
    }


    // 도지역 안의 도시이름(광역시 이상은 구)
    private ArrayList<RegionObj> subRegion(String provinceName) {
        ArrayList<RegionObj> regionObjs = new ArrayList<>();
        for (RegionObj obj : mRegionObjs) {
            if (obj.main.equals(provinceName)) {
                regionObjs.add(obj);
            }
        }

        return regionObjs;
    }

}
