package com.cl.cltv.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Case By:scene
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/6/6 11:10
 */

public class ApkUtils {

    /**
     * Case By: 获取apk文件的版本号
     * package:com.mzhguqvn.mzhguq.util
     * Author：scene on 2017/6/6 11:11
     *
     * @param context         上下文
     * @param archiveFilePath 文件绝对路径
     */
    public static int getVersionCodeFromApk(Context context, String archiveFilePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            int version = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            return 0;
        }
    }
}
