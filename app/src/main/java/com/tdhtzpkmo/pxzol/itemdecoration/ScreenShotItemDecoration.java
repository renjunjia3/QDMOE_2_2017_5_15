package com.tdhtzpkmo.pxzol.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Case By: 视频截图
 * package:
 * Author：scene on 2017/4/13 9:37
 */
public class ScreenShotItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public ScreenShotItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        //设置每行最后一个的右间距
        if (childLayoutPosition > 1 && childLayoutPosition < parent.getChildCount() - 1) {
            outRect.right = space;
        }
    }

}
