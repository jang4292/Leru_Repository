package com.bpm.bpm_ver4.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    private ArrayList<Fragment> fragments;
    private ArrayList<String> tags;

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String tag) {
        fragments.add(fragment);
        tags.add(tag);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
