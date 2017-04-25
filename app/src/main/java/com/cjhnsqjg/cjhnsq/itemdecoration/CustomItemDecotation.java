package com.cjhnsqjg.cjhnsq.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Case By:自定义的分割线
 * package:com.hfaufhreu.hjfeuio.itemdecoration
 * Author：scene on 2017/4/18 13:27
 */

public class CustomItemDecotation extends RecyclerView.ItemDecoration {
    private int verticalSpace;
    private int horizontalSpace;
    private int spaceNumber;
    private boolean isSpaceTop;


    /**
     * Case By:
     * Author: scene on 2017/4/18 13:38
     *
     * @param horizontalSpace 水平间距
     * @param verticalSpace   竖直
     * @param spaceNumber     每行的item个数
     * @param isSpaceTop      是否有顶部的间距
     */
    public CustomItemDecotation(int horizontalSpace, int verticalSpace, int spaceNumber, boolean isSpaceTop) {
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
        this.spaceNumber = spaceNumber;
        this.isSpaceTop = isSpaceTop;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = verticalSpace;
        outRect.right = horizontalSpace;
        //获取childPosition
        int childLayoutPosition = parent.getChildLayoutPosition(view);
        //设置每列第一个的左边距
        if (childLayoutPosition % spaceNumber == 0) {
            outRect.left = horizontalSpace;
        }
        //设置每列最后一个的右边距
        if (childLayoutPosition % spaceNumber == 1) {
            outRect.right = horizontalSpace;
        }

        //设置顶部行间距
        if (isSpaceTop) {
            if (childLayoutPosition < spaceNumber) {
                outRect.top = verticalSpace;
            }
        }
    }

}
