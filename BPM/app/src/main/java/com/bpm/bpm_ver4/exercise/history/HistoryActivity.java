package com.bpm.bpm_ver4.exercise.history;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.api.CommonUrl;
import com.bpm.bpm_ver4.databinding.ActivityMainBinding;
import com.bpm.bpm_ver4.util.view.ActionToggleUtil;
import com.bpm.bpm_ver4.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class HistoryActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private ActionToggleUtil toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(binding.layoutAppbar.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.layoutAppbar.tvTitleToolbar.setText(R.string.menu_history);

        toggle = new ActionToggleUtil(
                this, binding.navView,
                binding.drawerLayout, binding.layoutAppbar.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        HistoryCalendarFragment historyFragment = HistoryCalendarFragment.newInstance();
        ActivityUtils.replaceFragmentToActivity(getSupportFragmentManager(),
                historyFragment, R.id.fragment_container);
    }


    @Override
    protected void onResume() {
        super.onResume();
        toggle.setCheck(R.id.nav_history);
        View haderView = binding.navView.getHeaderView(0);
        ImageView ivProfile = haderView.findViewById(R.id.iv_profile_image);
        TextView tvNick = haderView.findViewById(R.id.tv_nick);

        int profileRes = R.drawable.ic_account_circlel;
        tvNick.setText(App.getMember().getInfo().getNickname());
        Glide.with(this)
                .load(CommonUrl.profileImageUrl + App.getMember().getInfo().getPhoto())
                .apply(new RequestOptions()
                        .placeholder(profileRes)
                        .error(profileRes)
                        .circleCrop())
                .into(ivProfile);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onBackPressed() {
        /* 네비게이션 드로우 닫고 RETURN */
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        /* 모든 조건에 맞지 않으면 실행 */
        super.onBackPressed();
    }
}
