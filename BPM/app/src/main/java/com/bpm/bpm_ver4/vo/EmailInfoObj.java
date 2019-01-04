package com.bpm.bpm_ver4.vo;

public class EmailInfoObj extends JsonObj {

    private long id; // 아이디
    private String email;    // 이메일 주소
    private String password; // 이메일 로그인 패스워드

    public EmailInfoObj() {
    }

    public EmailInfoObj(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
