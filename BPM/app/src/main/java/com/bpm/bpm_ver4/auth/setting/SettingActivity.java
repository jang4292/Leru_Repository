package com.bpm.bpm_ver4.auth.setting;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.Api;
import com.bpm.bpm_ver4.api.signin.SignInApi;
import com.bpm.bpm_ver4.auth.SplashActivity;
import com.bpm.bpm_ver4.databinding.ActivitySettingBinding;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.vo.ApiObj;

public class SettingActivity extends BaseActivity {


    private ActivitySettingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        setSupportActionBar(binding.topLayer.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topLayer.tvTitleToolbar.setText(getString(R.string.title_setting));

        // 각 Item 들이 RecyclerView 의 전체 크기를 변경하지 않는 다면
        // setHasFixedSize() 함수를 사용해서 성능을 개선할 수 있습니다.
        // 변경될 가능성이 있다면 false 로 , 없다면 true를 설정해주세요.
        binding.recyclerView.setHasFixedSize(true);

        // 차일드뷰 구분선 데코레이션 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),
                new LinearLayoutManager(this).getOrientation());
//        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        binding.recyclerView.addItemDecoration(dividerItemDecoration);

        // 리사이클러뷰 어뎁터 세팅
        LinearLayoutManager manager = new LinearLayoutManager(this);
        SettingRecyclerAdapter adapter = new SettingRecyclerAdapter(this,
                getSettingMenu());
        adapter.setOnItemClickListener(getOnItemClickListener());
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private String[] getSettingMenu() {
        String email = App.getEmail();
        if (email == null || email.isEmpty()) {
            return getResources().getStringArray(R.array.setting_menu_list_for_sns);
        } else {
            return getResources().getStringArray(R.array.setting_menu_list_for_email);
        }
    }


    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.popup_title_secession);
        builder.setMessage(R.string.popup_contents_secession);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String token = new AppPref(SettingActivity.this).getStringPref(AppPref.KEY_TOKEN);
            SignInApi.secession(token,
                    callback -> {
                        ApiObj apiObj = (ApiObj) callback;
                        if (apiObj.status.equals(Api.STATUS_OK)) {
                            showToast(R.string.menu_secession);
                            dialog.dismiss();
                            activityFinishToSplash();
                        }
                    });
        });
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private OnItemClickListener getOnItemClickListener() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                show();
            }
        };
    }


    private void activityFinishToSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
