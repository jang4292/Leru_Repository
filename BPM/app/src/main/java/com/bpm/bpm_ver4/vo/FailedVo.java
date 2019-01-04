package com.bpm.bpm_ver4.vo;

public class FailedVo {

    private int failedMessage;

    public FailedVo(){}

    public FailedVo(int failedMessage) {
        this.failedMessage = failedMessage;
    }

    public int getFailedMessage() {
        return failedMessage;
    }

    public void setFailedMessage(int failedMessage) {
        this.failedMessage = failedMessage;
    }
}
