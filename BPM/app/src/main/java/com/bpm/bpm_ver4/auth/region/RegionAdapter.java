package com.bpm.bpm_ver4.auth.region;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class RegionAdapter extends RecyclerView.Adapter implements RegionAdapterContract.Model, RegionAdapterContract.View {

    private Context mContext;
    private List<String> mProvince; // 광역시 및 도 이름
    private List<RegionObj> mRegionObjs; // 도지역/광역시에 속하는 지역리스트
    private OnItemClickListener mOnItemClickListener;

    private boolean isProvince = true;

    public RegionAdapter(Context context) {
        this.mContext = context;
        this.mProvince = new ArrayList<>();
        this.mRegionObjs = new ArrayList<>();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mContext, parent, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.onBinding(position);

        if (isProvince) {
            viewHolder.btnRegion.setText(mProvince.get(position));
        } else {
            viewHolder.btnRegion.setText(mRegionObjs.get(position).sub);
        }

    }

    @Override
    public int getItemCount() {
        if (isProvince) {
            return mProvince.size();
        }
        return mRegionObjs.size();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public boolean isProvince() {
        return isProvince;
    }

    @Override
    public void isProvince(boolean isProvince) {
        this.isProvince = isProvince;
    }

    @Override
    public void addProvince(List<String> provinces) {
        this.mProvince = provinces;
    }

    @Override
    public void addItems(ArrayList<RegionObj> regionObjs) {
        this.mRegionObjs = regionObjs;
    }

    @Override
    public String getProince(int position) {
        return mProvince.get(position);
    }

    @Override
    public RegionObj getItem(int position) {
        if (mRegionObjs != null && mRegionObjs.size() >= 1) {
            return mRegionObjs.get(position);
        }
        return null;
    }

    @Override
    public void clearItems() {
        if (mRegionObjs != null) {
            mRegionObjs.clear();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        private OnItemClickListener onItemClickListener;

        private Button btnRegion;

        public ViewHolder(Context context, ViewGroup parent, OnItemClickListener onItemClickListener) {
            super(LayoutInflater.from(context).inflate(R.layout.item_region, parent, false));

            this.context = context;
            this.onItemClickListener = onItemClickListener;
            btnRegion = itemView.findViewById(R.id.btn_region);
        }


        public void onBinding(int position) {
            itemView.setOnClickListener((v) -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }

    }
}
