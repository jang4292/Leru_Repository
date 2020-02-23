package com.bpm202.SensorProject.db;

import com.bpm202.SensorProject.ValueObject.JsonObj;

import java.util.ArrayList;

public class SendGrapthData extends JsonObj {

    public int idx;

    public String nickname; // 별명
    public String email;  // 지역 이름

    public int height;  // 키
    public int weight;  // 몸무게
    public int age;     // 나이

    public String gender;   // 성명 (남: M / 여: F)

    public int region;  // 지역
    public String regionStr;  // 지역 이름

    public ArrayList<SendScheduleData> list = new ArrayList<>();

}
