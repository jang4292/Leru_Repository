package com.bpm202.SensorProject.Data;

import android.support.annotation.NonNull;

import com.bpm202.SensorProject.ValueObject.RegionObj;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {
    private static RegionManager instance = null;

    private List<RegionObj> mainListData;
    private ArrayList<RegionClass> regionList;

    @NonNull
    public static RegionManager Instance() {
        if (instance == null) {
            instance = new RegionManager();
        }
        return instance;
    }

    public ArrayList<RegionClass> getData() {
        return regionList;
    }

    public void initData(List<RegionObj> list) {
        regionList = new ArrayList<>();
        mainListData = list;
        setMainRegionData(mainListData);
        setSubRegionData(mainListData);
    }

    private void setSubRegionData(List<RegionObj> objects) {
        for (RegionObj obj : objects) {
            for (RegionClass data : regionList) {
                if (obj.main.equals(data.getMainRegion())) {
                    data.addSubRegion(obj.sub);
                }
            }
        }
    }

    private void setMainRegionData(List<RegionObj> objects) {
        for (RegionObj obj : objects) {
            if (!hasDataInMainRegionList(obj.main, regionList)) {
                regionList.add(new RegionClass(obj.main));
            }
        }
    }


    private boolean hasDataInMainRegionList(String data, ArrayList<RegionClass> list) {
        for (RegionClass region : list) {
            if (data.equals(region.mainRegion)) {
                return true;
            }
        }
        return false;
    }

    public int getCurrentId(String mainRegion, String subRegion) {

        for (RegionObj obj : mainListData) {
            if (obj.main.equals(mainRegion) && obj.sub.equals(subRegion)) {
                return obj.id;
            }
        }

        return 0;
    }

    public class RegionClass {

        private String mainRegion;
        private ArrayList<String> subRegion = new ArrayList<>();

        public ArrayList<String> getSubRegion() {
            return subRegion;
        }

        public String getMainRegion() {
            return mainRegion;
        }

        public RegionClass(String main) {
            mainRegion = main;
        }

        public void addSubRegion(String region) {
            subRegion.add(region);
        }


    }
}
