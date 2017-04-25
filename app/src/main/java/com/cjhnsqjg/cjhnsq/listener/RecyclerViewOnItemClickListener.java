package com.cjhnsqjg.cjhnsq.listener;

import java.util.List;

/**
 * RecyclerView的itme点击时间
 * Created by scene on 2017/3/13.
 */

public interface RecyclerViewOnItemClickListener {
    void onItemClickListener(int position, List<?> list);
}
