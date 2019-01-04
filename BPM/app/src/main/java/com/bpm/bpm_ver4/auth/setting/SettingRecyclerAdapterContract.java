package com.bpm.bpm_ver4.auth.setting;


import com.bpm.bpm_ver4.util.OnItemClickListener;

public interface SettingRecyclerAdapterContract {

    interface Model {

    }

    interface View {
        void setOnItemClickListener(OnItemClickListener onItemClickListener);
    }

}
