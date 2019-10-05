package com.bpm202.SensorProject.Common;

public class CommonUrl {
    public static final String baseUrl = "http://yhjang.ipdisk.co.kr:8081/";
    public static final String url = "BuffUpSensor/";
    public static final String profileImageUrl = baseUrl + url + "images/profile/";
    public static final String personalInfoPhoto = url + "PersonalInfo/Photo"; // 프로필 이미지 업로드
    public static final String regionUrl = url + "Region";// 지역목록 조회
    public static final String emailDuplUrl = url + "CheckEmail";    // 이메일 중복 확인
    public static final String nickDuplUrl = url + "CheckNickname";// 닉네임 중복 확인
    public static final String signUpEmailUrl = url + "SignUpWithEmail";// 이메일 회원가입
    public static final String signUpSnsUrl = url + "SignUpWithSns";// sns 회원가입
    public static final String signInEmail = url + "SignInWithEmail";// 이메일 로그인
    public static final String signInSns = url + "SignInWithSns";// sns 로그인
    public static final String signInTokenUrl = url + "SignInToken";// 자동 로그인(token 로그인)
    public static final String modifyPersonalInfoUrl = url + "PersonalInfo";// 회원정보 수정
    public static final String findPasswordUrl = url + "Password";// 비밀번호 찾기
    public static final String changePasswordUrl = url + "Password";// 비밀변호 변경
    public static final String secessionUrl = url + "DeleteMe";// 회원탈퇴
    public static final String exerciseUrl = url + "Exercise";// 운동결과 등록, 운동 기록 조회
    public static final String exerciseMonthUrl = url + "Exercise/Month";// 운동기록 유무 조회
    public static final String exerciseDayUrl = url + "Exercise/Day";// 일별 운동 결과 조회
    public static final String scheduleAddUrl = url + "ScheduleAdd";// 일정추가
    public static final String scheduleUrl = url + "Schedule";// 일정 조회
    public static final String scheduleDeleteUrl = url + "ScheduleDelete";// 일정 삭제
    public static final String scheduleEditUrl = url + "ScheduleEdit";// 일정 수정
    public static final String scheduleSequenceUrl = url + "ScheduleSequence";// 일정순서 변경
}
