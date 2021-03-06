package com.bpm.bpm_ver4.exercise.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ExVo;
import com.bpm.bpm_ver4.testpack.FakeData;
import com.bpm.bpm_ver4.util.MappingUtil;

import java.util.ArrayList;
import java.util.List;

public class HistoryDateFragment extends BaseFragment {

    // view
    private ViewFlipper viewFlipper;
    private ImageButton ibtnPrev, ibtnNext;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;

    public static HistoryDateFragment newInstance() {
        HistoryDateFragment fragment = new HistoryDateFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_days, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        viewFlipper = view.findViewById(R.id.view_flipper);
        recyclerView1 = view.findViewById(R.id.recycler_view1);
        recyclerView2 = view.findViewById(R.id.recycler_view2);
        recyclerView3 = view.findViewById(R.id.recycler_view3);
        ibtnPrev = view.findViewById(R.id.ibtn_prev);
        ibtnNext = view.findViewById(R.id.ibtn_next);

        ibtnPrev.setOnClickListener(v -> {
            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
            viewFlipper.showPrevious();
        });
        ibtnNext.setOnClickListener(v -> {
            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
            viewFlipper.showNext();
        });

        List<ExVo> list1 = new ArrayList<>();
        list1.add(FakeData.getExerciseData());
        HistoryAdapter adapter1 = new HistoryAdapter(getContext(), list1);

        List<ExVo> list2 = new ArrayList<>();
        list2.add(FakeData.getExerciseData());
        list2.add(FakeData.getExerciseData());
        HistoryAdapter adapter2 = new HistoryAdapter(getContext(), list2);

        List<ExVo> list3 = new ArrayList<>();
        list3.add(FakeData.getExerciseData());
        list3.add(FakeData.getExerciseData());
        list3.add(FakeData.getExerciseData());
        HistoryAdapter adapter3 = new HistoryAdapter(getContext(), list3);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView1.addItemDecoration(decoration);
        recyclerView2.addItemDecoration(decoration);
        recyclerView3.addItemDecoration(decoration);
    }



    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private Context mContext;
        private List<ExVo> exVoList;

        public HistoryAdapter(Context context, List<ExVo> exVoList) {
            this.mContext = context;
            this.exVoList = exVoList;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(mContext, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.onBinding(position);
        }

        @Override
        public int getItemCount() {
            return exVoList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private Context context;
            private TextView tvExerciseName;
            private TextView tvWeightNum, tvRestNum, tvSetNum, tvCountNum;
            private TextView tvWeightLabel, tvRestLabel, tvSetLabel, tvCountLabel;
            private ImageButton ibtnDelete;
            private ImageView imMove;
            private int position;

            public ViewHolder(Context context, ViewGroup parent) {
                super(LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false));
                this.context = context;
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
                ExVo exVo = exVoList.get(position);

                tvExerciseName.setText(MappingUtil.name(mContext, exVo.getType().getName()));

                if (exVo.getType().isTime()) {
                    tvWeightLabel.setText(R.string.schedules_rpm);
                    tvCountLabel.setText("total\n");
                    tvCountLabel.append(getString(R.string.schedules_times));
                } else {
                    tvWeightLabel.setText(R.string.schedules_kg);
                    tvCountLabel.setText("total\n");
                    tvCountLabel.append(getString(R.string.schedules_count));
                }
                tvRestLabel.setText("total\n");
                tvRestLabel.append(getString(R.string.schedules_rest));
                tvSetLabel.setText("total\n");
                tvSetLabel.append(getString(R.string.schedules_set));

                tvCountNum.setText(String.format("%02d", exVo.getCount()));
                tvSetNum.setText(String.format("%02d", exVo.getSetCnt()));
                tvRestNum.setText(String.format("%02d", exVo.getRest()));
                tvWeightNum.setText(String.format("%02d", exVo.getWeight()));
            }
        }
    }


}















