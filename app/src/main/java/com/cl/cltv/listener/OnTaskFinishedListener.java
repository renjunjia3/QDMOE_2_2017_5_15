package com.cl.cltv.listener;

/**
 * Case By: 异步线程完成的监听器
 * package:com.qd.sqlitedem
 * Author：scene on 2017/5/27 14:06
 */
public interface OnTaskFinishedListener<T> {
    void onTaskFinished(T data);
}
