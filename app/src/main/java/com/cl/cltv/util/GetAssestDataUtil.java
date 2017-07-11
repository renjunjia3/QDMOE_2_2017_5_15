package com.cl.cltv.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Case By: 获取Assest文件夹的工具类
 * package:com.mzhguqvn.mzhguq.util
 * Author：scene on 2017/5/19 15:34
 */
public class GetAssestDataUtil {

    /**
     * Case By:获取json字符串
     * Author: scene on 2017/5/19 15:34
     */
    public static String getAssestJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * Case By:获取字符串
     * Author: scene on 2017/5/19 15:34
     */
    public static String getString(Context context, String fileName) {

        //StringBuilder stringBuilder = new StringBuilder();
        String result = "";
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));

            String line = "";
            while ((line = bf.readLine()) != null) {
                result += line;
                result+="\n\t";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}

