package com.bpm.bpm_ver4.testpack;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.BaseActivity;
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

public class GoogleLoginActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    private final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_google_login);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // 2018-07-20 : 여기에 UI 등 넣기
        findViewById(R.id.google_sign_in_btn).setOnClickListener(View -> signIn());
//        findViewById(R.id.google_sign_out_btn).setOnClickListener(View -> signOut());
//        findViewById(R.id.google_revocke_btn).setOnClickListener(View -> reokeAccess());
    }


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    // 로그인 결과에 따른 ui변환 넣기
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // 사용자 정보가 있음
        } else {
            // 사용자 정보가 없음
        }
    }


    // 구글 계정 로그인 및 가입
    private void signIn() {
        Intent signInIIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIIntent, RC_SIGN_IN);
    }

    // 구글 계정 로그아웃
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // 성공 처리
                    }
                });
    }


    // 구글 계정 탈퇴
    private void reokeAccess() {
        // Firebase sign out
        firebaseUserDelete();
        Log.d(TAG, "reokeAccess");

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInintent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    // 파이어베이스 구글 사용자 정보 등록
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}















