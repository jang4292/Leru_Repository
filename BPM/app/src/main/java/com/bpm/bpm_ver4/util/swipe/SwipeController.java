package com.bpm.bpm_ver4.util.swipe;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.View;

import com.bpm.bpm_ver4.App;
import com.bpm.bpm_ver4.Common;
import com.bpm.bpm_ver4.exercise.schedule.ExerciseSchedulesAdapter.ScheduleViewHolder;
import com.bpm.bpm_ver4.util.ItemTouchHelperAdapter;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeController extends Callback {

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private SwipeControllerActions buttonsActions = null;

    private static final float buttonWidth = 300;

    private ItemTouchHelperAdapter mAdapter;

    private boolean isEditMode;

    public SwipeController(ItemTouchHelperAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = UP | DOWN;
//        int swipeFlags = LEFT;
        int swipeFlags = 0;

        if (isEditMode)
            return makeMovementFlags(dragFlags, swipeFlags);
        else return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }


    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    private void actionLog(int actionState) {
        String action = "";
        switch (actionState) {
            case ACTION_STATE_IDLE :
                action = "ACTION_STATE_IDLE";
                break;
            case ACTION_STATE_SWIPE :
                action = "ACTION_STATE_SWIPE";
                break;
            case ACTION_STATE_DRAG :
                action = "ACTION_STATE_DRAG";
                break;
        }
        Log.d("ACTION" , action);
    }

    private RecyclerView.ViewHolder tempViewHolder;
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        actionLog(actionState);
        if (viewHolder != null && actionState == ACTION_STATE_DRAG) {
            tempViewHolder = viewHolder;
            viewHolder.itemView.setBackgroundColor(0xFFeeeeee);
        }

        if (tempViewHolder != null && actionState == ACTION_STATE_IDLE) {
            tempViewHolder.itemView.setBackgroundColor(0xFFffffff);
            tempViewHolder = null;
            buttonShowedState = ButtonsState.GONE;
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            drawButtons(c, viewHolder);
            if (buttonShowedState != ButtonsState.GONE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        } // end if(actionState == ACTION_STATE_SWIPE)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF rightButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText("삭제", c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = rightButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.right-(textWidth * 1.5f), button.centerY()+(textSize/2), p);
    }

}

