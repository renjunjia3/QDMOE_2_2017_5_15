package com.cl.cltv.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cl.cltv.bean.VideoInfo;

import java.util.List;

/**
 * Case By:
 * package:com.hfaufhreu.hjfeuio.itemdecoration
 * Author：scene on 2017/4/24 13:57
 */

public class DiamondItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private List<VideoInfo> list;
    private boolean hasFooter;

    public DiamondItemDecoration(int space, List<VideoInfo> list, boolean hasFooter) {
        this.space = space;
        this.list = list;
        this.hasFooter = hasFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int childLayoutPosition = parent.getChildLayoutPosition(view);

        if (hasFooter) {
            if (childLayoutPosition != 0 && childLayoutPosition <= list.size()) {
                outRect.bottom = space;
                outRect.right = space / 2;
                outRect.left = space / 2;
            }
        } else {
            if (childLayoutPosition != 0) {
                outRect.bottom = space;
                outRect.right = space / 2;
                outRect.left = space / 2;
            }
        }

    }
}
