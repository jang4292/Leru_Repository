package com.bpm202.SensorProject.ValueObject;

public class MemberObj extends JsonObj {

    private String id;   // 아이디

    private EmailInfoObj emailInfo;  // 이메일 정보
    private SnsInfoObj snsInfo;      // sns 정보

    private PersonalInfoObj info;    // 개인 정보


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EmailInfoObj getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(EmailInfoObj emailInfo) {
        this.emailInfo = emailInfo;
    }

    public SnsInfoObj getSnsInfo() {
        return snsInfo;
    }

    public void setSnsInfo(SnsInfoObj snsInfo) {
        this.snsInfo = snsInfo;
    }

    public PersonalInfoObj getInfo() {
        return info;
    }

    public void setInfo(PersonalInfoObj info) {
        this.info = info;
    }
}
