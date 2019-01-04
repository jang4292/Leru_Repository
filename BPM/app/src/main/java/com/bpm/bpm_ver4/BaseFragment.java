package com.bpm.bpm_ver4;


import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.bpm.bpm_ver4.util.AppPref;

import java.util.Objects;

public class BaseFragment extends Fragment {

    public String getToken() {
        return new AppPref(Objects.requireNonNull(getActivity())).getStringPref(AppPref.KEY_TOKEN);
    }

    public void showToast(int message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
