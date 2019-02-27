package com.bpm202.SensorProject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    @NonNull
    protected abstract int getLayoutId();

    @NonNull
    protected abstract void initView(View v);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false); // 여기서 UI를 생성해서 View를 return
        initView(v);
        return v;
    }
}
