package com.bpm.bpm_ver4.util.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.bpm.bpm_ver4.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class GoogleLoginHelper implements LoginInHelper {

    private final String TAG = getClass().getSimpleName();

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private Callback mCallback;

    private final int RC_SIGN_IN = 1;
    private Activity mActivity;

    /******************** interface  *****************************
     * 구글 계정 로그인 및 가입*/
    @Override
    public void loginPressImp() {
        Intent signInIIntent = mGoogleSignInClient.getSignInIntent();
        mActivity.startActivityForResult(signInIIntent, RC_SIGN_IN);
    }

    /******************** interface  *****************************
     * 구글 계정 로그아웃*/
    @Override
    public void logoutPressImp() {
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(mActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 성공 처리
                    }
                });
    }

    /******************** interface  *****************************
     * 구글계정 회원 탈퇴 */
    @Override
    public void secessionImp() {
        reokeAccess();
    }

    /******************** interface  *****************************
     * 시작시 로그인 기록이 있으면 자동 로그인*/
    @Override
    public void onStartImp() {
        // Check if user is signed in (non-null) and update UI accordingly.
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
            Log.e(TAG, "onStart + try");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "onStart + printStackTrace");
        }
    }

    /******************** interface  *****************************
     * 구글 로그인 요청에 대한 결과값*/
    // 로그인 요청에 대한 결과값 받음
    @Override
    public void onActivityResultImp(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInintent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account); // <---- 파이어베이스 구글 사용자 정보 등록
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }



    public GoogleLoginHelper(Activity activity, Callback googleLoginCallback) {
        this.mActivity = activity;
        this.mCallback = googleLoginCallback;
        init();
    }


    private void init() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(mActivity, gso);
        mAuth = FirebaseAuth.getInstance();
    }


    // 로그인 결과에 따른 ui변환 넣기
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // 사용자 정보가 있음
            Toast.makeText(mActivity.getApplicationContext(), currentUser.getUid(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "updateUI + not null");
            Log.d("getUid :", currentUser.getUid());
            Log.d("getIdToken :", String.valueOf(currentUser.getIdToken(true)));
            mCallback.callback(Callback.OK, currentUser.getUid());
        } else {
            // 사용자 정보가 없음
            Log.e(TAG, "updateUI + null");
        }
    }


    // 구글 계정 탈퇴
    private void reokeAccess() {
        // Firebase sign out
        firebaseUserDelete();
        Log.d(TAG, "reokeAccess");

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(mActivity,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 성공 처리
                    }
                });
    }



    // 파이어베이스 연동 정보 삭제
    private void firebaseUserDelete() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // 성공 처리

                            if (task.isSuccessful()) {
                                // 파이어베이스의 유저 정보 삭제
                            } else {
                                // 실패
                            }
                        }
                    });
        }
    }



    // 파이어베이스 구글 사용자 정보 등록
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(mActivity.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }




}