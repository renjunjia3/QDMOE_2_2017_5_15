package com.cl.cltv.base;

import android.content.Context;
import android.os.AsyncTask;

import com.cl.cltv.listener.OnTaskFinishedListener;


/**
 * Case By: 异步线程的基类
 * package:com.qd.sqlitedemo
 * Author：scene on 2017/5/27 14:06
 */
public abstract class BaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

    protected Context mContext;
    //异步线程完成的监听
    protected OnTaskFinishedListener<T> mOnTaskFinishedListener;

    public BaseAsyncTask(Context context, OnTaskFinishedListener<T> onTaskFinishedListener) {
        mContext = context;
        mOnTaskFinishedListener = onTaskFinishedListener;
    }

    @Override
    protected abstract T doInBackground(Void... params);

    @Override
    protected void onPostExecute(T data) {
        super.onPostExecute(data);
        //回调异步线程执行完成
        if (mOnTaskFinishedListener != null) {
            mOnTaskFinishedListener.onTaskFinished(data);
        }
    }
}
