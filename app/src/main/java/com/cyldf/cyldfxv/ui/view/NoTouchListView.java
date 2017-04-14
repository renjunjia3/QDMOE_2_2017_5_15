package com.cyldf.cyldfxv.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Case By:
 * package:com.yuuymd.kkskoo.ui.view
 * Author：scene on 2017/4/6 13:24
 */

public class NoTouchListView extends ListView {
    public NoTouchListView(Context context) {
        super(context);
    }

    public NoTouchListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoTouchListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
