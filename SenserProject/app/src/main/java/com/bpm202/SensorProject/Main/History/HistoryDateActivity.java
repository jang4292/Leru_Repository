package com.bpm202.SensorProject.Main.History;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bpm202.SensorProject.App;
import com.bpm202.SensorProject.Common.AppPreferences;
import com.bpm202.SensorProject.CustomView.CustomCalendar;
import com.bpm202.SensorProject.Data.ExDataSrouce;
import com.bpm202.SensorProject.Data.ExRepository;
import com.bpm202.SensorProject.Data.ExVo;
import com.bpm202.SensorProject.R;
import com.bpm202.SensorProject.Util.MappingUtil;
import com.bpm202.SensorProject.Util.Util;
import com.bpm202.SensorProject.ValueObject.TypeValueObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryDateActivity extends AppCompatActivity {


    private TextView tv_date;

    private String date;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = getIntent().getStringExtra(AppPreferences.KEY_DATE_INFO);
//        date = savedInstanceState.getString(AppPreferences.KEY_DATE_INFO);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_history_days);
        onLoaded();
        initView();
    }

    private void onLoaded() {
//        Util.FadeAnimation.fadeOut(gridview);
//        ExRepository.getInstance().getExerciseDay(String.valueOf(mYear), String.valueOf(mMonth),
        Log.d("TEST", "date : " + date);

//        StringBuffer buffer = new StringBuffer();
//
//        buffer.append(date.getsubstring(0,4));
//        buffer.append("-");
//        buffer.append(date.substring(4,2));
//        buffer.append("-");
//        buffer.append(date.substring(6,2));






//        String newDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

        ExRepository.getInstance().getExerciseDay(App.getToken(), date,
                new ExDataSrouce.GetDayCallback() {
                    @Override
                    public void onLoaded(List<ExVo> exVoList) {
                        Log.d("TEST", exVoList.toString());


                    }

                    @Override
                    public void onDataNotAvailable() {
                        Log.d("TEST", "onDataNotAvailable");
                    }
//                    @Override
//                    public void onLoaded(List<Integer> i_list) {
//                        adapter.notifyDataSetChanged(updateCalendar(), i_list);
//                        if (onTotalDaysTextViewListener != null) {
//                            TextView tv = onTotalDaysTextViewListener.getOnTotalDaysText();
//                            if (tv != null) {
//                                String str = i_list.size() + "DAYS";
//                                tv.setText(str);
//                            }
//                        }
//                        Util.FadeAnimation.fadeIn(gridview);
//                    }

//                    @Override
//                    public void onDataNotAvailable() {
//                        Util.FadeAnimation.fadeIn(gridview);
//                    }
                });
    }

    // view
    private ViewFlipper viewFlipper;
    private ImageButton ibtnPrev;
    private ImageButton ibtnNext;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;

//    public static HistoryDateActivity newInstance() {
//        HistoryDateActivity fragment = new HistoryDateActivity();
//        return fragment;
//    }


//    @NonNull
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_history_days;
//    }

    @NonNull
//    @Override
    protected void initView(/*View v*/) {
        viewFlipper = findViewById(R.id.view_flipper);

        tv_date = findViewById(R.id.tv_date);

        recyclerView1 = findViewById(R.id.recycler_view1);
        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView3 = findViewById(R.id.recycler_view3);
        ibtnPrev = findViewById(R.id.ibtn_prev);
        ibtnNext = findViewById(R.id.ibtn_next);

                ibtnPrev.setOnClickListener(v1 -> {
            viewFlipper.setInAnimation(this, R.anim.slide_in_right);
            viewFlipper.setOutAnimation(this, R.anim.slide_out_left);
            viewFlipper.showPrevious();
        });

                ibtnNext.setOnClickListener(v12 -> {
            viewFlipper.setInAnimation(this, R.anim.slide_in_left);
            viewFlipper.setOutAnimation(this, R.anim.slide_out_right);
            viewFlipper.showNext();
        });


                List<ExVo> list1 = new ArrayList<>();
        List<ExVo> list2 = new ArrayList<>();
        List<ExVo> list3 = new ArrayList<>();

        // Dummy data for TEST
        list1.add(getExerciseData());
        list2.add(getExerciseData());
        list2.add(getExerciseData());
        list3.add(getExerciseData());
        list3.add(getExerciseData());
        list3.add(getExerciseData());

        HistoryAdapter adapter1 = new HistoryAdapter(this, list1);
        HistoryAdapter adapter2 = new HistoryAdapter(this, list2);
        HistoryAdapter adapter3 = new HistoryAdapter(this, list3);

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);

        DividerItemDecoration decoration = new DividerItemDecoration(this, new LinearLayoutManager(this).getOrientation());
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView1.addItemDecoration(decoration);
        recyclerView2.addItemDecoration(decoration);
        recyclerView3.addItemDecoration(decoration);

//        viewFlipper = v.findViewById(R.id.view_flipper);
//        recyclerView1 = v.findViewById(R.id.recycler_view1);
//        recyclerView2 = v.findViewById(R.id.recycler_view2);
//        recyclerView3 = v.findViewById(R.id.recycler_view3);
//        ibtnPrev = v.findViewById(R.id.ibtn_prev);
//        ibtnNext = v.findViewById(R.id.ibtn_next);

//        ibtnPrev.setOnClickListener(v1 -> {
//            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_right);
//            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_left);
//            viewFlipper.showPrevious();
//        });
//
//
//        ibtnNext.setOnClickListener(v12 -> {
//            viewFlipper.setInAnimation(getContext(), R.anim.slide_in_left);
//            viewFlipper.setOutAnimation(getContext(), R.anim.slide_out_right);
//            viewFlipper.showNext();
//        });
//        List<ExVo> list1 = new ArrayList<>();
//        List<ExVo> list2 = new ArrayList<>();
//        List<ExVo> list3 = new ArrayList<>();
//
//        // Dummy data for TEST
//        list1.add(getExerciseData());
//        list2.add(getExerciseData());
//        list2.add(getExerciseData());
//        list3.add(getExerciseData());
//        list3.add(getExerciseData());
//        list3.add(getExerciseData());
//
//        HistoryAdapter adapter1 = new HistoryAdapter(getContext(), list1);
//        HistoryAdapter adapter2 = new HistoryAdapter(getContext(), list2);
//        HistoryAdapter adapter3 = new HistoryAdapter(getContext(), list3);
//
//        recyclerView1.setAdapter(adapter1);
//        recyclerView2.setAdapter(adapter2);
//        recyclerView3.setAdapter(adapter3);
//
//        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
//        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
//        recyclerView1.addItemDecoration(decoration);
//        recyclerView2.addItemDecoration(decoration);
//        recyclerView3.addItemDecoration(decoration);
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
                tvWeightNum = itemView.findViewById(R.id.tv_weight_num);
                tvRestNum = itemView.findViewById(R.id.tv_rest_num);
                tvSetNum = itemView.findViewById(R.id.tv_set_num);
                tvCountNum = itemView.findViewById(R.id.tv_count_num);
                tvWeightLabel = itemView.findViewById(R.id.tv_weight_label);
                tvRestLabel = itemView.findViewById(R.id.tv_rest_label);
                tvSetLabel = itemView.findViewById(R.id.tv_set_label);
                tvCountLabel = itemView.findViewById(R.id.tv_count_label);
                ibtnDelete = itemView.findViewById(R.id.ibtn_delete);
                imMove = itemView.findViewById(R.id.iv_move);
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

    // Dummy data for TEST
    public static ExVo getExerciseData() {
        Random random = new Random();
        TypeValueObject[] types = TypeValueObject.values();

        ExVo.Builder eb = new ExVo.Builder();
        eb.setCount(10);
        eb.setCountMax(10);
        eb.setWeight(100);
        eb.setDuration(20);
        eb.setDistance(25);
        eb.setAngle(0);
        eb.setBalance(15);
        eb.setSetCnt(1);
        eb.setSetMax(3);
        eb.setRest(5);
        eb.setType(types[random.nextInt(types.length - 1)]);

        return eb.create();
    }


}















