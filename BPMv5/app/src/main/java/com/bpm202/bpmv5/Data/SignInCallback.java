package com.bpm202.bpmv5.Data;

import com.bpm202.bpmv5.ValueObject.MemberObj;

public interface SignInCallback {

    void onResponse(String token, MemberObj memberObj);

    void onDataNotAvailable();
}
