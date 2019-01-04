package com.bpm.bpm_ver4.exercise.schedule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;
import com.bpm.bpm_ver4.util.ItemTouchHelperAdapter;
import com.bpm.bpm_ver4.util.MappingUtil;
import com.bpm.bpm_ver4.util.anim.AnimationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseSchedulesAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private Context mContext;
    private List<ScheduleVo> scheduleVos;
    private SchedulesFragment.OnItemClickListener onItemClickListener;
    private String mToken;
    private ScheduleRepository mRepository;
    private ItemTouchHelper mTouchHelper;
    private boolean isEditMode;

    public ExerciseSchedulesAdapter(Context context, String token) {
        this.mContext = context;
        this.scheduleVos = new ArrayList<>();
        this.mToken = token;
        this.mRepository = ScheduleRepository.getInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(mContext, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScheduleViewHolder viewHolder = (ScheduleViewHolder) holder;
        viewHolder.onBinding(position);

        if (isEditMode) {
            viewHolder.imMove.setVisibility(View.VISIBLE);
            viewHolder.ibtnDelete.setVisibility(View.VISIBLE);
            AnimationUtil.fadeIn(viewHolder.imMove, viewHolder.ibtnDelete);

            viewHolder.imMove.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (mTouchHelper != null) {
                            mTouchHelper.startDrag(holder);
                        }
                    }
                    return false;
                }
            });


            viewHolder.ibtnDelete.setOnClickListener(v -> {
                onItemDismiss(holder.getAdapterPosition());
            });

            viewHolder.itemView.setOnClickListener(null);
        } else {
            AnimationUtil.fadeOut(viewHolder.imMove, viewHolder.ibtnDelete);

            viewHolder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(scheduleVos.get(holder.getAdapterPosition()),  ExerciseSchedulesAdapter.this);
            });
        }
    }

    @Override
    public int getItemCount() {
        return scheduleVos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setScheduleVos(List<ScheduleVo> scheduleVos) {
        if (scheduleVos != null) {
            this.scheduleVos = scheduleVos;
            notifyDataSetChanged();
        }
    }


    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public void setOnItemClickListener(SchedulesFragment.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void setTouchHellper(ItemTouchHelper itemTouchHelper) {
        this.mTouchHelper = itemTouchHelper;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(scheduleVos, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(scheduleVos, i, i - 1);
            }
        }

        for (int i = 0; i < scheduleVos.size(); i++) {
            scheduleVos.get(i).setPos(i +1);
        }
        mRepository.sequenceSchedules(scheduleVos);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mRepository.deleteSchedule(scheduleVos.get(position));
        this.scheduleVos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onRefresh() {

    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private TextView    tvExerciseName;
        private TextView    tvWeightNum, tvRestNum, tvSetNum, tvCountNum;
        private TextView    tvWeightLabel, tvRestLabel, tvSetLabel, tvCountLabel;
        private ImageButton ibtnDelete;
        private ImageView   imMove;

        private ScheduleViewHolder(Context context, ViewGroup parent) {
            super(LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false));
            tvExerciseName = itemView.findViewById(R.id.tv_exercise_name);
            tvWeightNum    = itemView.findViewById(R.id.tv_weight_num);
            tvRestNum      = itemView.findViewById(R.id.tv_rest_num);
            tvSetNum       = itemView.findViewById(R.id.tv_set_num);
            tvCountNum     = itemView.findViewById(R.id.tv_count_num);
            tvWeightLabel  = itemView.findViewById(R.id.tv_weight_label);
            tvRestLabel    = itemView.findViewById(R.id.tv_rest_label);
            tvSetLabel     = itemView.findViewById(R.id.tv_set_label);
            tvCountLabel   = itemView.findViewById(R.id.tv_count_label);
            ibtnDelete     = itemView.findViewById(R.id.ibtn_delete);
            imMove         = itemView.findViewById(R.id.iv_move);
        }


        private void onBinding(int position) {
            ScheduleVo scheduleVo = scheduleVos.get(position);

            tvExerciseName.setText(MappingUtil.name(mContext, scheduleVo.getType().getName()));

            if (scheduleVo.getType().isTime()) {
                tvWeightLabel.setText(R.string.schedules_rpm);
                tvCountLabel.setText(R.string.schedules_times);
            } else {
                tvWeightLabel.setText(R.string.schedules_kg);
                tvCountLabel.setText(R.string.schedules_count);
            }
            tvRestLabel.setText(R.string.schedules_rest);
            tvSetLabel.setText(R.string.schedules_set);

            tvCountNum.setText(String.format("%02d", scheduleVo.getCount()));
            tvSetNum.setText(String.format("%02d", scheduleVo.getSetCnt()));
            tvRestNum.setText(String.format("%02d", scheduleVo.getRest()));
            tvWeightNum.setText(String.format("%02d", scheduleVo.getWeight()));



        }
    }


}
