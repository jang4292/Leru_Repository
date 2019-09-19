package com.bpm202.SensorProject.Data;

import com.bpm202.SensorProject.ValueObject.MemberObj;

public interface SignInCallback {

    void onResponse(String token, MemberObj memberObj);

    void onDataNotAvailable();
}
