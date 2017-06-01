package com.mzhguqvn.mzhguq.task;


import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.mzhguqvn.mzhguq.base.BaseAsyncTask;
import com.mzhguqvn.mzhguq.bean.ChannelResultInfo;
import com.mzhguqvn.mzhguq.listener.OnTaskFinishedListener;

/**
 * Case By:解析频道数据
 * package:com.mzhguqvn.mzhguq.task
 * Author：scene on 2017/6/1 16:38
 */

public class ChannelTask extends BaseAsyncTask<ChannelResultInfo> {
    private String jsonStr;

    public ChannelTask(Context context, OnTaskFinishedListener<ChannelResultInfo> onTaskFinishedListener, String s) {
        super(context, onTaskFinishedListener);
        jsonStr = s;
    }

    @Override
    protected ChannelResultInfo doInBackground(Void... params) {
        try {
            return JSON.parseObject(jsonStr, ChannelResultInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
