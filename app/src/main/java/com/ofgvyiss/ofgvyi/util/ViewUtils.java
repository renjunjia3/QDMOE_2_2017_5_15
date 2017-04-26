package com.ofgvyiss.ofgvyi.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 处理View的工具类
 * Created by scene on 2017/3/14.
 */

public class ViewUtils {

    public static void setViewHeightByLinearLayout(View view, int height) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = height;// 控件的高强制设成20
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    public static void setViewHeightByRelativeLayout(View view, int height) {
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        linearParams.height = height;// 控件的高强制设成20
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    public static void setViewHeightByViewGroup(View view, int height) {
        ViewGroup.LayoutParams linearParams = (ViewGroup.LayoutParams) view.getLayoutParams();
        linearParams.height = height;// 控件的高强制设成20
        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }

    public static void setDialogViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }
}
