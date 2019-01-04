package com.bpm.bpm_ver4.util;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

    void onRefresh();
}
