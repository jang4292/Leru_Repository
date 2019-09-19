package com.bpm202.SensorProject.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.SignUpDataSource;
import com.bpm202.SensorProject.Data.SignUpRepository;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.ValueObject.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class RegionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RegionAdapter mRegionAdapter;
    private Toolbar toolbar;

    private List<RegionObj> regionObjList;

    private boolean is2ndDepth = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setContentView(R.layout.activity_region);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_location_select);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        recyclerView = findViewById(R.id.recycler_view);

        mRegionAdapter = new RegionAdapter(RegionActivity.this);
        recyclerView.setAdapter(mRegionAdapter);
    }

    /**
     * 도 지역 추출(또는 광역시 이상의 시)
     */
    private List<RegionObj> mainRegion(List<RegionObj> regionObjs) {
        List<RegionObj> provinceName = new ArrayList<>();
        String temp = "";
        for (RegionObj obj : regionObjs) {
            if (!temp.equals(obj.main)) {
                temp = obj.main;
                provinceName.add(obj);
            }
        }

        return provinceName;
    }

    /**
     * 도지역 안의 도시이름(광역시 이상은 구)
     *
     * @param provinceName
     * @return
     */
    private ArrayList<RegionObj> subRegion(String provinceName) {
        ArrayList<RegionObj> regionObjs = new ArrayList<>();
        for (RegionObj obj : regionObjList) {
            if (obj.main.equals(provinceName)) {
                regionObjs.add(obj);
            }
        }

        return regionObjs;
    }

    private void initData() {
        SignUpRepository.getInstance().region(new SignUpDataSource.RegionCallback() {

            @Override
            public void onResponse(List<RegionObj> objList) {
                regionObjList = objList;
                mRegionAdapter.setRegionObjs(mainRegion(regionObjList));
            }

            @Override
            public void onDataNotAvailable() {
                QToast.showToast(RegionActivity.this, R.string.error_msg);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (is2ndDepth) {
            is2ndDepth = false;
            mRegionAdapter.setRegionObjs(mainRegion(regionObjList));
        } else {
            super.onBackPressed();
        }
    }

    private class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

        private Context context;
        private List<RegionObj> regionObjs;

        public RegionAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_region, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.onBinding(position);
        }

        @Override
        public int getItemCount() {
            return regionObjs != null ? regionObjs.size() : 0;
        }

        public void setRegionObjs(List<RegionObj> regionObjs) {
            this.regionObjs = regionObjs;
            notifyDataSetChanged();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private final Button btnRegion;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                btnRegion = itemView.findViewById(R.id.btn_region);
            }

            public void onBinding(int position) {
                if (is2ndDepth) {
                    btnRegion.setText(regionObjs.get(position).sub);
                } else {
                    btnRegion.setText(regionObjs.get(position).main);
                }

                btnRegion.setOnClickListener(v -> {
                    if (is2ndDepth) {
                        Intent intent = new Intent();
                        intent.putExtra(CommonData.REGION_ID, regionObjs.get(position).id);
                        intent.putExtra(CommonData.REGION_NAME, String.format("%s %s", regionObjs.get(position).main, regionObjs.get(position).sub));
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        is2ndDepth = true;
                        setRegionObjs(subRegion(regionObjs.get(position).main));
                    }
                });
            }
        }
    }
}
