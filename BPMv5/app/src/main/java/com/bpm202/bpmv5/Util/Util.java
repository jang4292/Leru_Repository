package com.bpm202.bpmv5.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    // 이메일 형식검사를 한다.

    /**
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
