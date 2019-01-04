package com.bpm.bpm_ver4.data.source.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bpm.bpm_ver4.BuildConfig;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.signin.SignInInterface;
import com.bpm.bpm_ver4.api.signin.SignInRetrofit;
import com.bpm.bpm_ver4.data.source.SignUpDataSource;
import com.bpm.bpm_ver4.vo.ApiObj;
import com.bpm.bpm_ver4.vo.MemberObj;
import com.bpm.bpm_ver4.vo.RegionObj;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpRemoteDataSource implements SignUpDataSource {

    public static final String TAG = SignUpRemoteDataSource.class.getSimpleName();

    private SignInInterface mSignInRetrofit;

    public SignUpRemoteDataSource() {
        mSignInRetrofit = SignInRetrofit.getInstance();
    }

    // 서버에서 지역 목록 다운로드
    @Override
    public void region(@NonNull RegionCallback callback) {
        mSignInRetrofit.region()
                .enqueue(new Callback<ApiObj<List<RegionObj>>>() {
                    @Override
                    public void onResponse(Call<ApiObj<List<RegionObj>>> call, Response<ApiObj<List<RegionObj>>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " region", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " region", "not Data");
                            }
                        }

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " region", "null Data");
                            return;
                        }

                        ApiObj apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " region", "STATUS_FAIL");
                            return;
                        }

                        List<RegionObj> regionObjs = (List<RegionObj>) apiObj.obj;
                        callback.onResponse(regionObjs);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<List<RegionObj>>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " region", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void checkEmail(@NonNull String email, @NonNull CheckEmailCallback callback) {
        mSignInRetrofit.checkEmail(email)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " checkEmail", response.body().toJson());
                                Log.e(TAG + " checkEmail", response.headers().get("EmailCode"));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " checkEmail", "not Data");
                            }
                        }

                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " checkEmail", "null Data");
                            return;
                        }

                        ApiObj apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " checkEmail", "STATUS_FAIL");
                            Log.e(TAG + " checkEmail", apiObj.message);
                            return;
                        }

                        if (response.headers() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " checkEmail", "header null_1");
                            return;
                        }

                        String autoCode = response.headers().get("EmailCode");
                        Boolean enable = (Boolean) apiObj.obj;
                        callback.onResponse(autoCode, enable);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " checkEmail", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void checkNickname(@NonNull String nickname, @NonNull CheckNicknameCallback callback) {
        mSignInRetrofit.checkNickname(nickname)
                .enqueue(new Callback<ApiObj<Boolean>>() {
                    @Override
                    public void onResponse(Call<ApiObj<Boolean>> call, Response<ApiObj<Boolean>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " checkNickname", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " checkNickname", "not Data");
                            }
                        }


                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " checkNickname", "null Data");
                            return;
                        }

                        ApiObj apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " checkNickname", "STATUS_FAIL");
                            return;
                        }

                        Boolean enable = (Boolean) apiObj.obj;
                        callback.onResponse(enable);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<Boolean>> call, Throwable t) {

                    }
                });
    }

    @Override
    public void photo(@Nullable String thumbnail, @NonNull String filePath, @NonNull PhotoCallback callback) {
        File file = new File(filePath);

        Map<String, RequestBody> map = new HashMap<>();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        map.put("photo", requestBody);

        mSignInRetrofit.photo(thumbnail, map)
                .enqueue(new Callback<ApiObj<String>>() {
                    @Override
                    public void onResponse(Call<ApiObj<String>> call, Response<ApiObj<String>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " photo", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " photo", "not Data");
                            }
                        }


                        if (response == null || response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " photo", "null Data");
                            return;
                        }

                        ApiObj apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " photo", "STATUS_FAIL");
                            return;
                        }


                        String path = (String) apiObj.obj;
                        callback.onResponse(path);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<String>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " photo", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void signUpWithEmail(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback) {
        mSignInRetrofit.signUpWithEmail(memberObj)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " signUpWithEmail", response.headers().get("Authorization"));
                                Log.e(TAG + " signUpWithEmail", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " signUpWithEmail", "not Data");
                            }
                        }

                        if (response == null || response.headers() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithEmail", "header null");
                            return;
                        }

                        String token = response.headers().get("Authorization");
                        if (token == null || token.isEmpty()) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithEmail", "header null");
                            return;
                        }

                        if (response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithEmail", "null Data");
                            return;
                        }

                        ApiObj<MemberObj> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithEmail", "STATUS_FAIL");
                            return;
                        }


                        MemberObj obj = apiObj.obj;
                        callback.onResponse(token, obj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " signUpWithEmail", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }

    @Override
    public void signUpWithSns(@NonNull MemberObj memberObj, @NonNull SignUpCallback callback) {
        mSignInRetrofit.signUpWithSns(memberObj)
                .enqueue(new Callback<ApiObj<MemberObj>>() {
                    @Override
                    public void onResponse(Call<ApiObj<MemberObj>> call, Response<ApiObj<MemberObj>> response) {
                        if (BuildConfig.DEBUG) {
                            try {
                                Log.e(TAG + " signUpWithSns", response.headers().get("Authorization"));
                                Log.e(TAG + " signUpWithSns", response.body().toJson());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG + " signUpWithSns", "not Data");
                            }
                        }


                        if (response == null || response.headers() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithSns", "header null");
                            return;
                        }

                        String token = response.headers().get("Authorization");
                        if (token == null || token.isEmpty()) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithSns", "header null");
                            return;
                        }

                        if (response.body() == null) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithSns", "null Data");
                            return;
                        }

                        ApiObj<MemberObj> apiObj = response.body();
                        if (apiObj.status.equals(Api.STATUS_FAIL)) {
                            callback.onDataNotAvailable();
                            Log.e(TAG + " signUpWithSns", "STATUS_FAIL");
                            return;
                        }


                        MemberObj obj = apiObj.obj;
                        callback.onResponse(token, obj);
                    }

                    @Override
                    public void onFailure(Call<ApiObj<MemberObj>> call, Throwable t) {
                        t.printStackTrace();
                        Log.e(TAG + " signUpWithSns", "onFailure");
                        callback.onDataNotAvailable();
                    }
                });
    }
}
