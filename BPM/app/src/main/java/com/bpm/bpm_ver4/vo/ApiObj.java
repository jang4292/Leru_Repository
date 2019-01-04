package com.bpm.bpm_ver4.vo;

public class ApiObj<J> extends JsonObj {


    public J obj;
    public String message;  // 오류 메시지
    public String status;   // ok 또는 fail
    public String date;     // 날짜
}
