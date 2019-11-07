package com.bpm202.SensorProject.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bpm202.SensorProject.Data.CommonData;
import com.bpm202.SensorProject.Data.RegionManager;
import com.bpm202.SensorProject.Data.SignUpDataSource;
import com.bpm202.SensorProject.Data.SignUpRepository;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.QToast;
import com.bpm202.SensorProject.ValueObject.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class RegionActivity extends AppCompatActivity {
    private RecyclerView recyclerMainView;
    private RecyclerView recyclerSubView;

    private RegionMainAdapter mRegionMainAdapter;
    private RegionSubAdapter mRegionSubAdapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {

        setContentView(R.layout.activity_region);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.textColor, null));
        toolbar.setTitle(R.string.title_location_select);

        recyclerMainView = findViewById(R.id.recycler_main_view);
        mRegionMainAdapter = new RegionMainAdapter(RegionActivity.this);
        recyclerMainView.setAdapter(mRegionMainAdapter);

        recyclerSubView = findViewById(R.id.recycler_sub_view);
        mRegionSubAdapter = new RegionSubAdapter(RegionActivity.this);
        recyclerSubView.setAdapter(mRegionSubAdapter);
    }

    private void initData() {
        SignUpRepository.getInstance().region(new SignUpDataSource.RegionCallback() {

            @Override
            public void onResponse(List<RegionObj> objList) {
                RegionManager.Instance().initData(objList);
                mRegionMainAdapter.setRegionObjs(RegionManager.Instance().getData(), mRegionSubAdapter);
            }

            @Override
            public void onDataNotAvailable() {
                QToast.showToast(RegionActivity.this, R.string.error_msg);
            }
        });
    }

    private class RegionMainAdapter extends RecyclerView.Adapter<RegionMainAdapter.ViewHolder> {

        private Context context;
        private ArrayList<RegionManager.RegionClass> region;
        private RegionSubAdapter subAdapter;

        public RegionMainAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_region, viewGroup, false);
            return new ViewHolder(itemView, region);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.onBinding(position);
        }

        @Override
        public int getItemCount() {
            return region != null ? region.size() : 0;
        }

        public void setRegionObjs(ArrayList<RegionManager.RegionClass> data, RegionSubAdapter subAdapter) {
            this.region = data;
            this.subAdapter = subAdapter;
            notifyDataSetChanged();
        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private ArrayList<RegionManager.RegionClass> list;
            private final Button btnRegion;

            public ViewHolder(@NonNull View itemView, ArrayList<RegionManager.RegionClass> arrayList) {
                super(itemView);
                btnRegion = itemView.findViewById(R.id.btn_region);
                list = arrayList;
            }

            public void onBinding(int position) {
                btnRegion.setText(list.get(position).getMainRegion());
                btnRegion.setOnClickListener(v -> {
                    subAdapter.setRegionObjs(list.get(position).getMainRegion(), list.get(position).getSubRegion());
                });
            }
        }
    }

    private class RegionSubAdapter extends RecyclerView.Adapter<RegionSubAdapter.ViewHolder> {

        private Context context;
        private ArrayList<String> region;
        private String mainRegionName;

        public RegionSubAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_region, viewGroup, false);
            return new ViewHolder(itemView, mainRegionName, region);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            viewHolder.onBinding(mainRegionName, region, position);
        }

        @Override
        public int getItemCount() {
            return region != null ? region.size() : 0;
        }

        public void setRegionObjs(String mainRegion, ArrayList<String> data) {
            mainRegionName = mainRegion;
            this.region = data;
            notifyDataSetChanged();
        }


        private class ViewHolder extends RecyclerView.ViewHolder {

            private ArrayList<String> list;
            private final Button btnRegion;
            private String mainRegionName;

            public ViewHolder(@NonNull View itemView, String mainRegion, ArrayList<String> arrayList) {
                super(itemView);
                mainRegionName = mainRegion;
                btnRegion = itemView.findViewById(R.id.btn_region);
                list = arrayList;
            }

            public void onBinding(String mainRegion, ArrayList<String> arrayList, int position) {
                mainRegionName = mainRegion;
                list = arrayList;

                btnRegion.setText(list.get(position));
                btnRegion.setOnClickListener(v -> {
                    Intent intent = new Intent();
//                        intent.putExtra(CommonData.REGION_ID, regionObjs.get(position).id);
//                        intent.putExtra(CommonData.REGION_NAME, String.format("%s %s", regionObjs.get(position).main, regionObjs.get(position).sub));
                    intent.putExtra(CommonData.REGION_ID, RegionManager.Instance().getCurrentId(mainRegionName, list.get(position)));
                    intent.putExtra(CommonData.REGION_NAME, String.format("%s %s", mainRegionName, list.get(position)));
                    setResult(RESULT_OK, intent);
                    finish();
                });
            }
        }
    }
}

