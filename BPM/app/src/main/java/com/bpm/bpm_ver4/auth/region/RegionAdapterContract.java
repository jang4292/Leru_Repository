package com.bpm.bpm_ver4.auth.region;

import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.util.ArrayList;
import java.util.List;

public interface RegionAdapterContract {

    interface View {
        void setOnItemClickListener(OnItemClickListener onItemClickListener);
        void notifyAdapter();
    }

    interface Model {
        boolean isProvince();
        void isProvince(boolean isProvince);
        void addProvince(List<String> provinces);
        void addItems(ArrayList<RegionObj> regionObjs);
        String getProince(int position);
        RegionObj getItem(int position);
        void clearItems();
    }
}
