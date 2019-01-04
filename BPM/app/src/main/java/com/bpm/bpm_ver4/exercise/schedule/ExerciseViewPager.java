package com.bpm.bpm_ver4.exercise.schedule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpm.bpm_ver4.BaseFragment;
import com.bpm.bpm_ver4.R;
import com.bpm.bpm_ver4.data.entity.ScheduleVo;
import com.bpm.bpm_ver4.data.source.ScheduleDataSource;
import com.bpm.bpm_ver4.data.source.ScheduleRepository;
import com.bpm.bpm_ver4.util.Observer;
import com.bpm.bpm_ver4.util.swipe.SwipeController;
import com.bpm.bpm_ver4.vo.DayOfWeek;

import java.util.List;

public class ExerciseViewPager extends BaseFragment implements Observer {

    private RecyclerView recyclerView;
    private ExerciseSchedulesAdapter adapter;
    private SchedulesFragment.OnItemClickListener onItemClickListener;
    private ItemTouchHelper mItemTouchHelper;
    private SwipeController swipeController;


    public static ExerciseViewPager newInstance() {
        ExerciseViewPager fragment = new ExerciseViewPager();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedules_pager_view, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());
        adapter = new ExerciseSchedulesAdapter(getContext(), getToken());
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.hasFixedSize();
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider_shape));
        recyclerView.addItemDecoration(decoration);

        swipeController = new SwipeController(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setTouchHellper(itemTouchHelper);
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_exercise_list);
    }

    public void setSchedule(List<ScheduleVo> scheduleVos) {
        adapter.setScheduleVos(scheduleVos);
    }

    public int getScheduleCount() {
        return adapter.getItemCount();
    }


    public void setOnItemClickListener(SchedulesFragment.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void update(Object arg) {
        boolean isEditMode = (boolean) arg;
        swipeController.setEditMode(isEditMode);
        adapter.setEditMode(isEditMode);
        adapter.notifyDataSetChanged();
    }
}
