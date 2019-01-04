package com.bpm.bpm_ver4.vo;

public class SnsInfoObj extends JsonObj {

    private long id;     // 아이디
    private String sns;   // SNS명
    private String token;  // 토큰 등의 식별 가능한 키

    public SnsInfoObj() {

    }

    public SnsInfoObj(String sns, String token) {
        this.sns = sns;
        this.token = token;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSns() {
        return sns;
    }

    public void setSns(String sns) {
        this.sns = sns;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
