package com.bpm202.bpmv5.ValueObject;

public class RegionObj extends JsonObj {

    public int id;  // 아이디
    public String main; // 시/도
    public String sub;  // 시/군/구

    public RegionObj(int id, String main, String sub) {
        this.id = id;
        this.main = main;
        this.sub = sub;
    }
}
