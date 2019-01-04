package com.bpm.bpm_ver4.auth.region;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.databinding.ActivityRegionBinding;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;

public class RegionActivity extends BaseActivity implements RegionContract.View {

    private ActivityRegionBinding binding;

    private RegionContract.Presenter mPresenter;
    private RegionPresenter mRegionPresenter;

    private RegionAdapter mRegionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_region);

        // 어뎁터 생성 리사이클러뷰 바인딩
        mRegionAdapter = new RegionAdapter(this);
        binding.recyclerView.setAdapter(mRegionAdapter);

        // Presenter 생성 + 바인딩
        mRegionPresenter = new RegionPresenter(this);
        mRegionPresenter.setAdapterView(mRegionAdapter);
        mRegionPresenter.setAdapterModel(mRegionAdapter);

        // 툴바생성, 툴바에 홈버튼(뒤로가기) 추가
        binding.topLayer.toolbar.setTitle(R.string.title_location_select);
        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegionPresenter.importRegionList();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        mPresenter.backPress();
    }

    @Override
    public void regionListUpdateView(ArrayList<RegionObj> regionObjs) {

    }

    // 지역값을 호출한 액티비티에 반환한다.
    @Override
    public void regionSelectedToReturn(RegionObj regionObj) {
        Intent intent = new Intent();
        intent.putExtra(Common.REGION_ID, regionObj.id);
        intent.putExtra(Common.REGION_NAME, String.format("%s %s", regionObj.main, regionObj.sub));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showMessage(int message) {
        showToast(message);
    }

    @Override
    public void showMessage(String message) {
        showToast(message);
    }

    @Override
    public void backPress() {
        super.onBackPressed();
    }

    @Override
    public void setPresenter(RegionContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
