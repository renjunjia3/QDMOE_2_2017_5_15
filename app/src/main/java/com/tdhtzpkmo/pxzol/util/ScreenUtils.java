package com.tdhtzpkmo.pxzol.util;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * @FileName:com.scene.baselib.util.ScreenUtils.java
 * @功能描述： 屏幕工具类
 * @author: scene
 * @date: 2016-07-27 14:36
 */
public class ScreenUtils {
    private Context mCtx;
    private static ScreenUtils mScreenTools;

    public static ScreenUtils instance(Context ctx) {
        if (null == mScreenTools) {
            mScreenTools = new ScreenUtils(ctx);
        }
        return mScreenTools;
    }

    private ScreenUtils(Context ctx) {
        mCtx = ctx.getApplicationContext();
    }

    public int getScreenWidth() {
        return mCtx.getResources().getDisplayMetrics().widthPixels;
    }

    public float dip2px(float dip) {
        float density = getDensity(mCtx);
        return dip * density + 0.5f;
    }

    public int dp2px(float dp) {
        float density = getDensity(mCtx);
        return (int) (dp * density + 0.5);
    }

    public float px2dip(float px) {
        float density = getDensity(mCtx);
        return (int) ((px - 0.5) / density);
    }

    private float getDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density;
    }


    /**
     * ５40 的分辨率上是85 （
     *
     * @return
     */
    public float getScal() {
        return getScreenWidth() * 100 / 480;
    }

    /**
     * 宽全屏, 根据当前分辨率　动态获取高度
     * height 在８００*４８０情况下　的高度
     *
     * @return
     */
    public float get480Height(float height480) {
        int width = getScreenWidth();
        return (height480 * width / 480);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public float getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = mCtx.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public float getScreenHeight() {
        return mCtx.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public float sp2px(float spValue) {
        final float fontScale = mCtx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public float px2sp(float pxValue) {
        final float fontScale = mCtx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
