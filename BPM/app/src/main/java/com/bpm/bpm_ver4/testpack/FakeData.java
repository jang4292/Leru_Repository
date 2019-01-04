package com.bpm.bpm_ver4.testpack;

import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.data.entity.ExVo;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.DayOfWeek;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.PersonalInfoObj;
import com.bpm.bpm_ver4.vo.RegionObj;
import com.bpm.bpm_ver4.vo.ScheduleObj;
import com.bpm.bpm_ver4.vo.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class FakeData {

    public static final boolean TEST = false;

    public static ArrayList<RegionObj> getFakeRegionData() {
        ArrayList<RegionObj> objs = new ArrayList<>();

        objs.add(new RegionObj(1, "대구", "동구"));
        objs.add(new RegionObj(2, "대구", "서구"));
        objs.add(new RegionObj(3, "대구", "남구"));
        objs.add(new RegionObj(4, "대구", "북구"));
        objs.add(new RegionObj(5, "대구", "수성구"));
        objs.add(new RegionObj(6, "대구", "달서구"));

        return objs;
    }


    public static ApiObj<MemberObj> getEmialLoginData() {
        PersonalInfoObj info = new PersonalInfoObj();
        info.setNickname("홍길동");
        info.setGender(Api.MALE);
        info.setHeight(180);
        info.setWeight(80);
        info.setAge(27);
        info.setRegion(1);
        ApiObj<MemberObj> obj = new ApiObj<>();
        obj.date = new SimpleDateFormat("yyyy.MM.dd hh.mm.ss").format(new Date());
        obj.status = Api.STATUS_OK;
        obj.message = "테스트용 가짜 데이터";
        obj.obj = new MemberObj();
        obj.obj.setInfo(info);

        return obj;
    }


    public static ApiObj<ArrayList<ScheduleObj>> getScheduleData() {
        ArrayList<ScheduleObj> objs = new ArrayList<>();
        Random random = new Random();
        DayOfWeek[] days = DayOfWeek.values();
        Type[] types = Type.values();
        for (int i = 0;  i < 5;  i++) {
            ScheduleObj obj = new ScheduleObj();
            obj.setPos(i+1);
            obj.setRest(7);
            obj.setCount(7);
            obj.setSetCnt(random.nextInt(4)+1);
            obj.setWeight(random.nextInt(99)+1);
            obj.setDay(days[4]);
            obj.setType(types[random.nextInt(types.length - 1)]);
            objs.add(obj);
        }
        ApiObj<ArrayList<ScheduleObj>> apiObj = new ApiObj();
        apiObj.obj = objs;
        apiObj.message = "테스트용 가짜 데이터";
        apiObj.status = Api.STATUS_OK;
        apiObj.date = new SimpleDateFormat("yyyy.MM.dd hh.mm.ss").format(new Date());
        return apiObj;
    }


    public static ExVo getExerciseData() {
        Random random = new Random();
        Type[] types = Type.values();

        ExVo.Builder eb = new ExVo.Builder();
        eb.setCount(10);
        eb.setCountMax(10);
        eb.setWeight(100);
        eb.setDuration(20);
        eb.setDistance(25);
        eb.setAngle(0);
        eb.setBalance(15);
        eb.setSetCnt(1);
        eb.setSetMax(3);
        eb.setRest(5);
        eb.setType(types[random.nextInt(types.length-1)]);

        return eb.create();
    }


    public static int set = 0;
    public static ExVo getExerciseData2() {
        Type[] types = Type.values();
        set++;

        ExVo.Builder eb = new ExVo.Builder();
        eb.setCount(10);
        eb.setCountMax(10);
        eb.setWeight(100);
        eb.setDuration(20);
        eb.setDistance(25);
        eb.setAngle(0);
        eb.setBalance(15);
        eb.setSetCnt(set);
        eb.setSetMax(5);
        eb.setRest(5);
        eb.setType(types[0]);

        return eb.create();
    }
}
