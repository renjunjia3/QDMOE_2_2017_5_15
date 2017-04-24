package com.tdhtzpkmo.pxzol.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 首页
 * Created by scene on 2017/3/14.
 */

public class IndexItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public IndexItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        //设置每行最后一个的右间距
        if (childLayoutPosition == 0) {
            outRect.top = space;
        }

    }

}
