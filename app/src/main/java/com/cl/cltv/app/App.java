package com.cl.cltv.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.aitangba.swipeback.ActivityLifecycleHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.cl.cltv.util.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * APP初始化
 */
public class App extends Application {
    public static String IMEI = "";
    public static int CHANNEL_ID = 0;
    public static int user_id = 0;
    //会员等级
    public static int role = 0;
    public static int cdn = 0;
    //试看次数
    public static int tryCount = 0;

    public static boolean isNeedCheckOrder = false;
    public static int orderIdInt = 0;
    public static int goodsOrderId = 0;
    public static boolean isGoodsPay = false;
    public static boolean isGoodsBuyPage = false;
    public static String order_id = "";

    //用户id
    public static final String USERID_KEY = "user_id";
    public static final String ROLE_KEY = "role_key";
    public static final String CDN_KEY = "cdn_key";
    public static final String TRY_COUNT_KEY = "try_count";


    //上一次的登录时间
    public static final String LAST_LOGIN_TIME = "last_login_time";

    public static int versionCode = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        CHANNEL_ID = getChannelName();
        user_id = SharedPreferencesUtil.getInt(this, USERID_KEY, 0);
        tryCount = SharedPreferencesUtil.getInt(this, TRY_COUNT_KEY, 0);
        //友盟
        MobclickAgent.setDebugMode(false);
        //activity滑动返回
        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
        //设置okhttp
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));

        try {
            versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Case By:获取渠道
     * Author: scene on 2017/5/19 10:46
     */
    private int getChannelName() {
        int resultData = 0;
        try {
            PackageManager packageManager = getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getInt("CHANNEL");
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }
}
