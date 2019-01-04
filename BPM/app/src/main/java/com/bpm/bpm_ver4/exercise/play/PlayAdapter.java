package com.bpm.bpm_ver4.exercise.play;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.util.OnItemClickListener;
import com.bpm.bpm_ver4.util.anim.CircleView;

import java.util.ArrayList;
import java.util.List;

public class PlayAdapter extends RecyclerView.Adapter
        implements PlayAdapterContract.Model, PlayAdapterContract.View {

    private Context mContext;
    private List<ScheduleVo> schedulesVos;
    private OnItemClickListener onItemClickListener;
    private View.OnTouchListener onTouchListener;

    private boolean touchEnable = true;

    public PlayAdapter(Context context) {
        this.mContext = context;
        this.schedulesVos = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mContext, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.onBinding(position);
    }

    @Override
    public int getItemCount() {
        return schedulesVos.size();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void setOnTouchListener(View.OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    @Override
    public void setTouchEnable(boolean enable) {
        this.touchEnable = enable;
    }

    @Override
    public void notifyView() {
        notifyDataSetChanged();
    }

    @Override
    public void setSchedule(List<ScheduleVo> scheduleVos) {
        this.schedulesVos = scheduleVos;
        notifyDataSetChanged();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private CircleView ibtnIcon;
        private TextView tvName;

        private ViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.item_play_select, parent, false));
            ibtnIcon = itemView.findViewById(R.id.ibtn_exercise);
        }


        private void onBinding(int position) {
            ScheduleVo scheduleVo = schedulesVos.get(position);
            itemView.setOnTouchListener(onTouchListener);
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    if (touchEnable) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            if (scheduleVo.isSuccess()) {
                ibtnIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_example_run_black));
            } else {
                ibtnIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_example_run));
            }
        }
    }

}
