package com.bpm202.SensorProject.RetrofitAPI;


import com.bpm202.SensorProject.Common.CommonUrl;
import com.bpm202.SensorProject.ValueObject.ApiObj;
import com.bpm202.SensorProject.ValueObject.EmailInfoObj;
import com.bpm202.SensorProject.ValueObject.MemberObj;
import com.bpm202.SensorProject.ValueObject.PersonalInfoObj;
import com.bpm202.SensorProject.ValueObject.RegionObj;
import com.bpm202.SensorProject.ValueObject.SnsInfoObj;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;


public interface SignInInterface {

    // 프로필 이미지 업로드
    @Multipart
    @POST(CommonUrl.personalInfoPhoto)
    Call<ApiObj<String>> photo(
            @Header("PreviousContents") String thumbnail,
            @PartMap Map<String, RequestBody> photo
    );

    // 지역목록 조회
    @GET(CommonUrl.regionUrl)
    Call<ApiObj<List<RegionObj>>> region();

    // 이메일 중복 확인
    @GET(CommonUrl.emailDuplUrl)
    Call<ApiObj<Boolean>> checkEmail(
            @Query("email") String email
    );

    // 닉네임 중복 확인
    @GET(CommonUrl.nickDuplUrl)
    Call<ApiObj<Boolean>> checkNickname(
            @Query("nickname") String nick
    );


    // 이메일 회원가입
    @POST(CommonUrl.signUpEmailUrl)
    Call<ApiObj<MemberObj>> signUpWithEmail(
            @Body MemberObj memberObj
    );

    // sns 회원가입
    @POST(CommonUrl.signUpSnsUrl)
    Call<ApiObj<MemberObj>> signUpWithSns(
            @Body MemberObj memberObj
    );

    // 이메일 로그인
    @POST(CommonUrl.signInEmail)
    Call<ApiObj<MemberObj>> signInWithEmail(
            @Body EmailInfoObj emailInfoObj
    );

    // sns 로그인
    @POST(CommonUrl.signInSns)
    Call<ApiObj<MemberObj>> signInWithSns(
            @Body SnsInfoObj snsInfoObj
    );


    // 자동 로그인(token 로그인)
    @POST(CommonUrl.signInTokenUrl)
    Call<ApiObj<MemberObj>> signInToken(
            @Header("Authorization") String token
    );


    // 회원정보 수정
    @POST(CommonUrl.modifyPersonalInfoUrl)
    Call<ApiObj<PersonalInfoObj>> modifyAccount(
            @Header("Authorization") String token,
            @Body() PersonalInfoObj obj
    );

    // 비밀번호 찾기
    @GET(CommonUrl.findPasswordUrl)
    Call<ApiObj<Boolean>> findPassword(
            @Query("email") String email
    );


    // 비밀번호 변경
    @POST(CommonUrl.changePasswordUrl)
    Call<ApiObj<Boolean>> changePassword(
            @Header("Authorization") String token,
            @Body() EmailInfoObj obj
    );


    //회원탈퇴
    @POST(CommonUrl.secessionUrl)
    Call<ApiObj<Boolean>> secession(
            @Header("Authorization") String token
    );
}
