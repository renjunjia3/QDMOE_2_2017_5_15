package com.mzhguqvn.mzhguq.util;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Case By: 图片下载工具类
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/5/19 15:35
 */
public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);

    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}  