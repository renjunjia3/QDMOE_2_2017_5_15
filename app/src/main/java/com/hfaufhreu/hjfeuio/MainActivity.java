package com.hfaufhreu.hjfeuio;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hfaufhreu.hjfeuio.app.App;
import com.hfaufhreu.hjfeuio.bean.UserInfo;
import com.hfaufhreu.hjfeuio.ui.fragment.MainFragment;
import com.hfaufhreu.hjfeuio.util.API;
import com.hfaufhreu.hjfeuio.util.DateUtils;
import com.hfaufhreu.hjfeuio.util.ScreenUtils;
import fm.jiecao.jcvideoplayer_lib.SharedPreferencesUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;
import okhttp3.Call;

/**
 * 类知乎 复杂嵌套Demo tip: 多使用右上角的"查看栈视图"
 * Created by scene on 16/6/2.
 */
public class MainActivity extends SupportActivity {
    private TextView toasrContent;
    private Timer mTimer;
    private Random random;
    private final Handler mHandler = new MyHandler(this);
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance());
        }
        loginAndRegister();
        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }

            @Override
            public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
                super.onFragmentCreated(fragment, savedInstanceState);
            }
            // 省略其余生命周期方法
        });

        random = new Random();
        mTimer = new Timer();
        mTimer.schedule(timerTask, random.nextInt(2000) + 1000 * 30, random.nextInt(60 * 1000) + 30 * 1000);
    }

    private void showNoticeToast(int id) {
        if (toast == null) {
            View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_toast, null);
            toasrContent = (TextView) v.findViewById(R.id.content);
            toast = new Toast(MainActivity.this);
            toast.setDuration(3000);
            toast.setView(v);
            toast.setGravity(Gravity.TOP, 0, (int) ScreenUtils.instance(MainActivity.this).dip2px(80));
        }
        toasrContent.setText("恭喜VIP" + id + "成为永久钻石会员");
        toast.show();
    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(10);
        }
    };

    class MyHandler extends Handler {
        WeakReference<Activity> mActivityReference;

        MyHandler(Activity activity) {
            mActivityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            showNoticeToast(random.nextInt(100000) + 10000);
        }
    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }


    private void loginAndRegister() {

        App.TRY_COUNT = SharedPreferencesUtil.getInt(MainActivity.this, App.TRY_COUNT_KEY, 0);

        TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        App.IMEI = tm.getDeviceId();
        long lastLoginTime = SharedPreferencesUtil.getLong(MainActivity.this, App.LAST_LOGIN_TIME, 0);
        if (!DateUtils.isDifferentDay(System.currentTimeMillis(), lastLoginTime)) {
            App.USER_ID = SharedPreferencesUtil.getInt(MainActivity.this, App.USERID_KEY, 0);
            App.ISSPEED = SharedPreferencesUtil.getInt(MainActivity.this, App.ISSPEED_KEY, 0);
            App.ISVIP = SharedPreferencesUtil.getInt(MainActivity.this, App.ISVIP_KEY, 0);
            return;
        }
        OkHttpUtils.get().url(API.URL_PRE + API.LOGIN_REGISTER + App.CHANNEL_ID + "/" + App.IMEI).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        e.printStackTrace();
                        loginAndRegister();
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        try {
                            UserInfo info = JSON.parseObject(s, UserInfo.class);
                            App.USER_ID = info.getUser_id();
                            App.ISVIP = info.getIs_vip();
                            App.ISSPEED = info.getIs_jiasu();
                            SharedPreferencesUtil.putInt(MainActivity.this, App.USERID_KEY, App.USER_ID);
                            SharedPreferencesUtil.putInt(MainActivity.this, App.ISSPEED_KEY, App.ISSPEED);
                            SharedPreferencesUtil.putInt(MainActivity.this, App.ISVIP_KEY, App.ISVIP);
                            SharedPreferencesUtil.putLong(MainActivity.this, App.LAST_LOGIN_TIME, System.currentTimeMillis());
                        } catch (Exception e) {
                            e.printStackTrace();
                            loginAndRegister();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
