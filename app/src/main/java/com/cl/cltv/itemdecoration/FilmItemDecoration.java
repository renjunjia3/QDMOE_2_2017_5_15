package com.cl.cltv.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 片库
 * Created by scene on 2017/3/14.
 */

public class FilmItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public FilmItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = space;
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);

        //设置每一行第一个的左间距
        if (childLayoutPosition % 3 == 0) {
            outRect.left = space;
            outRect.right = space / 3;
        }
        //设置每行中间的间距
        if (childLayoutPosition % 3 == 1) {
            outRect.right = (int) (space * 2f / 3f);
            outRect.left = (int) (space * 2f / 3f);
        }
        //设置每行最后一个的右间距
        if (childLayoutPosition % 3 == 2) {
            outRect.right = space;
            outRect.left = space / 3;
        }

    }

}
