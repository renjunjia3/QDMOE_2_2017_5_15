package com.cl.cltv.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 主播的分割线
 * Created by scene on 2017/3/14.
 */

public class AnchorItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public AnchorItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        outRect.bottom = space;
        if (childLayoutPosition % 2 == 1 && childLayoutPosition > 0 ) {
            outRect.right = space;
        }

    }

}
