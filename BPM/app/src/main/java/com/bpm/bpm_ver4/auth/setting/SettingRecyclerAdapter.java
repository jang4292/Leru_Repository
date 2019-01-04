package com.bpm.bpm_ver4.auth.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.util.AppPref;
import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.auth.account.AccountActivity;
import com.bpm.bpm_ver4.auth.account.ChangePasswordActivity;
import com.bpm.bpm_ver4.auth.login.LoginActivity;

import com.bpm.bpm_ver4.databinding.ItemSettingBinding;

public class SettingRecyclerAdapter extends RecyclerView.Adapter
        implements SettingRecyclerAdapterContract.Model, SettingRecyclerAdapterContract.View {

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private String[] mMenuStrArr;

    public SettingRecyclerAdapter(Context context, String[] menuStrArr) {
        this.mContext = context;
        this.mMenuStrArr = menuStrArr;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.binding.tvLabel.setText(mMenuStrArr[position]);

        viewHolder.binding.getRoot().setOnClickListener(v -> {

            if (mMenuStrArr[position].equals(mContext.getString(R.string.menu_change_password))) {
                mContext.startActivity(new Intent(mContext, ChangePasswordActivity.class));
                return;
            }

            if (mMenuStrArr[position].equals(mContext.getString(R.string.menu_my_info))) {
                mContext.startActivity(new Intent(mContext, AccountActivity.class));
                return;
            }

            if (mMenuStrArr[position].equals(mContext.getString(R.string.menu_logout))) {
                new AppPref(mContext.getApplicationContext()).setStringPref(AppPref.KEY_TOKEN, "");
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intent);
                return;
            }

            if (mMenuStrArr[position].equals(mContext.getString(R.string.menu_secession))) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
                return;
            }

            Toast.makeText(mContext.getApplicationContext(),
                    viewHolder.binding.tvLabel.getText().toString(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mMenuStrArr.length;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        ItemSettingBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

//        private Context context;
//        private OnItemClickListener onItemClickListener;
//        ItemSettingBinding binding;
//
//        public ViewHolder(Context context, ViewGroup parent, OnItemClickListener onItemClickListener) {
//            super(LayoutInflater.from(context).inflate(R.layout.item_setting, parent, false));
//            this.context = context;
//            this.onItemClickListener = onItemClickListener;
//            binding = DataBindingUtil.bind(itemView);
//        }
//
//        public void onBinding(int position) {
//
//        }
    }
}
