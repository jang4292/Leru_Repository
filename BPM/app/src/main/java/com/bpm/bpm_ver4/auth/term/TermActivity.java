package com.bpm.bpm_ver4.auth.term;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bpm.bpm_ver4.BaseActivity;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.auth.signup.SignUpSplashActivity;
import com.bpm.bpm_ver4.databinding.ActivityTermBinding;

public class TermActivity extends BaseActivity {


    private ActivityTermBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_term);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView title = findViewById(R.id.tv_title_toolbar);
        title.setText(R.string.title_term);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.btnNext.setOnClickListener(view -> startActivity(new Intent(this, SignUpSplashActivity.class)));

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
}
