package com.hfaufhreu.hjfeuio.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 首页其他分类的间隔
 * Created by scene on 2017/3/14.
 */

public class IndexOtherTypeItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public IndexOtherTypeItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        //设置第一行的顶部间距
        if (childLayoutPosition < 3) {
            outRect.top = space;
        }

        //设置每行第一个的左间距
        if (childLayoutPosition % 3 == 0) {
            outRect.left = space;
        }
        //设置每行最后一个的右间距
        if (childLayoutPosition % 3 == 2) {
            outRect.right = space;
        }

    }

}
