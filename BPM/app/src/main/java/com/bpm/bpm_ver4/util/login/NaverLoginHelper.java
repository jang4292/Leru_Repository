package com.bpm.bpm_ver4.util.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bpm.bpm_ver4.R;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

public class NaverLoginHelper implements LoginInHelper {

    private String TAG = getClass().getSimpleName();

    private Activity mActivity;

    private OAuthLogin mOAuthLoginInstance;
    private Context mContext;

    private Callback mCallback;

    private OAuthLoginButton mAuthLoginButton;
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                // 인증 성공
                String token = mOAuthLoginInstance.getAccessToken(mContext);
                Log.e(TAG, "mOAuthLoginHandler=" + token);
                Toast.makeText(mActivity.getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                tokenLog();

            } else {
                // 인증 실패
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Log.d(TAG + "errorCode: ", errorCode);
                Log.d(TAG + "errorDesc: ", errorDesc);
            }
        }
    };


    /******************** interface  *****************************
     * */
    @Override
    public void loginPressImp() {
        mOAuthLoginInstance.startOauthLoginActivity(mActivity, mOAuthLoginHandler);
    }
    /******************** interface  ******************************/
    @Override
    public void logoutPressImp() {
        mOAuthLoginInstance.logout(mContext);
    }
    /******************** interface  ******************************/
    @Override
    public void secessionImp() {
        new DeleteTokenTask().execute();
    }
    /******************** interface  ******************************/
    @Override
    public void onStartImp() {

    }
    /******************** interface  ******************************/
    @Override
    public void onActivityResultImp(int requestCode, int resultCode, Intent data) {

    }



    public NaverLoginHelper(Activity activity, Callback naverLoginCallback) {
        this.mActivity = activity;
        this.mContext = mActivity;
        this.mCallback = naverLoginCallback;
//        onCreate();
    }


    private void onCreate() {
        OAuthLoginDefine.DEVELOPER_VERSION = true;

        mOAuthLoginInstance = OAuthLogin.getInstance();
        // TODO: 2018-07-24 : client 값들 네이버 개발자센터에서 가져와야함 : 수정 필요
        mOAuthLoginInstance.init(

                mActivity,
                mContext.getString(R.string.naver_oauth_client_id),
                mContext.getString(R.string.naver_oauth_client_secret),
                mContext.getString(R.string.naver_oauth_client_name)
        );
    }


    // 연동 해제
    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            } else {
                Log.d(TAG, "성공");
            }

            return null;
        }
        protected void onPostExecute(Void v) {
            tokenLog();
        }
    }



    private void tokenLog() {

        try {
            // 로그인 결과로 얻은 접근 토큰(access token)을 반환합니다.
            Log.d(TAG + "getAccessToken: ", mOAuthLoginInstance.getAccessToken(mContext));
            Log.d(TAG + "getRefreshToken: ", mOAuthLoginInstance.getRefreshToken(mContext));
            // 접근 토큰(access token)의 만료 시간을 반환합니다.
            Log.d(TAG + "getExpiresAt: ", String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));

            Log.d(TAG + "getTokenType: ", mOAuthLoginInstance.getTokenType(mContext));
            Log.d(TAG + "getState: ", mOAuthLoginInstance.getState(mContext).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
